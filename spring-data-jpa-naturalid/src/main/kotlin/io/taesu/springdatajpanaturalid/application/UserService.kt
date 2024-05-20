package io.taesu.springdatajpanaturalid.application

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
}

class Result(
    val user: User,
    val loadedPersistenceContext: Boolean,
)
