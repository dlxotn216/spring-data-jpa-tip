package io.taesu.springdatajpanaturalid.domain

import jakarta.persistence.EntityManager
import jakarta.persistence.EntityNotFoundException
import jakarta.persistence.PersistenceContext
import org.hibernate.CacheMode
import org.hibernate.ScrollMode
import org.hibernate.Session
import org.hibernate.SessionFactory

/**
 * Created by itaesu on 2024/05/20.
 *
 * @author Lee Tae Su
 * @version spring-data-jpa-naturalid
 * @since spring-data-jpa-naturalid
 */
interface UserRepositoryCustom {
    fun getReferenceByEmail(email: String): User
    fun updateAllWithScroll()
    fun updateAllWithStatelessSession()
    fun upsert(users: List<User>)
}

class UserRepositoryCustomImpl(
    @field:PersistenceContext
    private val entityManager: EntityManager,
): UserRepositoryCustom {
    override fun getReferenceByEmail(email: String): User {
        // Composite NaturalId라면
        // entityManager.unwrap(Session::class.java)
        //     .byNaturalId(User::class.java)
        //     .using(User::name, name)
        //     .using(User::email, email)
        //     .load()

        return entityManager.unwrap(Session::class.java)
            .bySimpleNaturalId(User::class.java)
            .load(email)
            ?: throw EntityNotFoundException("User not found by email: $email")
    }

    // txn = entityManager.getTransaction();
    // txn.begin(); 으로 직접 여는경우
    // Not allowed to create transaction on shared EntityManager - use Spring transactions or EJB CMT instead
    // 서비스에서 @Transactional로 열어야 함
    override fun updateAllWithScroll() {
        val session = entityManager.unwrap(Session::class.java)
        session
            .createSelectionQuery("select u from User u", User::class.java)
            .setCacheMode(CacheMode.IGNORE)
            .scroll(ScrollMode.FORWARD_ONLY)
            .use {
                val batchSize = 100
                it.setFetchSize(batchSize)

                var count = 0
                while (it.next()) {
                    val user: User = it.get()
                    user.intro = "Hello ${it.rowNumber}"
                    session.merge(user)

                    if (++count % batchSize == 0) {
                        entityManager.flush();
                        entityManager.clear();
                    }
                }
            }
    }

    override fun updateAllWithStatelessSession() {
        val sessionFactory = entityManager.entityManagerFactory.unwrap(SessionFactory::class.java)
        sessionFactory.inStatelessSession { session ->
            session
                .createSelectionQuery("select u from User u", User::class.java)
                .scroll(ScrollMode.FORWARD_ONLY)
                .use {
                    val batchSize = 100
                    it.setFetchSize(batchSize)

                    while (it.next()) {
                        val user: User = it.get()
                        user.intro = "Hello ${it.rowNumber}"
                        session.update(user)
                        // session.upsert(user)
                    }
                }

        }
        // sessionFactory.openStatelessSession().use { session ->
        //     session
        //         .createSelectionQuery("select u from User u", User::class.java)
        //         .scroll(ScrollMode.FORWARD_ONLY)
        //         .use {
        //             val batchSize = 100
        //             it.setFetchSize(batchSize)
        //
        //             while (it.next()) {
        //                 val user: User = it.get()
        //                 user.intro = "Hello ${it.rowNumber}"
        //                 session.update(user)
        //                 // session.upsert(user)
        //             }
        //         }
        // }
    }

    override fun upsert(users: List<User>) {
        val sessionFactory = entityManager.entityManagerFactory.unwrap(SessionFactory::class.java)
        sessionFactory.inStatelessSession {session ->
            users.forEach {
                // insert 시 email이 빠짐
                // email에 unique=true, updatable=false, NaturalId 제거해야 정상 동작 함.
                // h2는 update 후 결과를 보고 insert 처리 함
                session.upsert(it)

            }
        }
    }
}
