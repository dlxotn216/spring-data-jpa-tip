package io.taesu.springdatajpanaturalid.domain

import jakarta.persistence.EntityManager
import jakarta.persistence.EntityNotFoundException
import jakarta.persistence.PersistenceContext
import org.hibernate.Session

/**
 * Created by itaesu on 2024/05/20.
 *
 * @author Lee Tae Su
 * @version spring-data-jpa-naturalid
 * @since spring-data-jpa-naturalid
 */
interface UserRepositoryCustom {
    fun getReferenceByEmail(email: String): User
}

class UserRepositoryCustomImpl(
    @field:PersistenceContext
    private val entityManager: EntityManager,
): UserRepositoryCustom {
    override fun getReferenceByEmail(email: String): User {
        return entityManager.unwrap(Session::class.java)
            .bySimpleNaturalId(User::class.java)
            .load(email)
            ?: throw EntityNotFoundException("User not found by email: $email")
    }
}
