package io.taesu.springdatajpanaturalid.application

import io.taesu.springdatajpanaturalid.domain.Ticket
import io.taesu.springdatajpanaturalid.domain.User
import io.taesu.springdatajpanaturalid.domain.UserRepository
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Created by itaesu on 2024/05/20.
 *
 * @author Lee Tae Su
 * @version spring-data-jpa-naturalid
 * @since spring-data-jpa-naturalid
 */
@Service
class UserService(
    private val userRepository: UserRepository,
    @field:PersistenceContext
    private val entityManager: EntityManager,
) {
    @Transactional(readOnly = true)
    fun retrieve(key: Long): Result {
        val referenceById = userRepository.getReferenceById(key)
        repeat(100) {
            userRepository.getReferenceById(key)
        }

        return Result(
            referenceById,
            entityManager.contains(referenceById)
        )
    }

    @Transactional(readOnly = true)
    fun retrieve(email: String): Result {
        val findByEmail = userRepository.findByEmail(email)
        repeat(100) {
            userRepository.findByEmail(email)
        }

        return Result(
            findByEmail,
            entityManager.contains(findByEmail)
        )
    }

    @Transactional(readOnly = true)
    fun retrieveEnhanced(email: String): Result {
        val findByEmail = userRepository.getReferenceByEmail(email)
        repeat(100) {
            userRepository.getReferenceByEmail(email)
        }

        return Result(
            findByEmail,
            entityManager.contains(findByEmail)
        )
    }

    @Transactional(readOnly = true)
    fun retrieveEnhancedWithNaturalId(email: String): Result {
        val findByEmail = userRepository.naturalId(email)
        repeat(100) {
            userRepository.naturalId(email)
        }

        return Result(
            findByEmail,
            entityManager.contains(findByEmail)
        )
    }

    @Transactional
    fun save(email: String): Pair<Long, Long> {
        return userRepository.save(
            User(
                email = email,
                name = "Tae Su, Lee",
            ).apply {
                addTicket("Spring Data JPA")
                addTicket("Spring Data JPA NaturalId")
            }
        ).let {
            it.key to it.tickets[0].key
        }
    }

    @Transactional(readOnly = true)
    fun lzayCollectionTest(userKey: Long, ticketKey: Long, index: Int): LazyCollectionResult {
        val user = userRepository.getReferenceById(userKey)
        return LazyCollectionResult(
            user.ticketsSize,
            user.containsTicket(Ticket("", user, key = ticketKey)),
            user.getTicket(index),
            user.getAll()
        )
    }

    @Transactional(readOnly = true)
    fun lzayCollectionLoadAll(userKey: Long): List<Ticket> {
        val user = userRepository.getReferenceById(userKey)
        return user.getAll()
    }

    @Transactional
    fun addTicket(userKey: Long) {
        val user = userRepository.getReferenceById(userKey)
        user.addTicket("Spring Data JPA")
    }

    @Transactional
    fun updateAllWithScroll() {
        userRepository.updateAllWithScroll()
    }

    @Transactional
    fun updateAllWithStatelessSession() {
        userRepository.updateAllWithStatelessSession()
    }

    @Transactional
    fun updateAllWithRepository() {
        userRepository.findAll()
            .forEach {
                it.intro = "Hello ${it.email} 22"
            }
    }

    @Transactional
    fun upsert(users: List<User>) {
        userRepository.upsert(users)
    }
}

class Result(
    val user: User,
    val loadedPersistenceContext: Boolean,
)

data class LazyCollectionResult(
    val size: Int,
    val contains: Boolean,
    val indexAt: Ticket,
    val all: List<Ticket>,
)
