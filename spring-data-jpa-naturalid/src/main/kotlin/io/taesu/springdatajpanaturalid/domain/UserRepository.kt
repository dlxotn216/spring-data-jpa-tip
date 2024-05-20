package io.taesu.springdatajpanaturalid.domain

import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, Long> , UserRepositoryCustom{
    fun findByEmail(email: String): User
}
