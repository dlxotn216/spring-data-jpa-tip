package io.taesu.springdatajpanaturalid.domain

import org.springframework.stereotype.Repository

@Repository
interface UserRepository: NaturalIdRepository<User, Long, String> , UserRepositoryCustom {
    fun findByEmail(email: String): User
}
