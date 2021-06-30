package io.taesu.demo.jparepositories.user.application

import io.taesu.demo.jparepositories.role.application.RoleRetrieveService
import io.taesu.demo.jparepositories.user.domain.User
import io.taesu.demo.jparepositories.user.domain.UserRepository
import io.taesu.demo.jparepositories.user.interfaces.UserCreateRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

/**
 * Created by itaesu on 2021/06/30.
 *
 * @author Lee Tae Su
 * @version TBD
 * @since TBD
 */
@Service
class UserCreateService(
    private val userRepository: UserRepository,
    private val roleRetrieveService: RoleRetrieveService
) {
    @Transactional
    fun create(request: UserCreateRequest): User {
        val toUser = request.toUser(LocalDateTime.now()).apply {
            this.add(roleRetrieveService.retrieve(request.roleKeys))
        }
        return userRepository.save(toUser)
    }

    @Transactional
    fun create(requests: List<UserCreateRequest>): List<User> {
        return userRepository.saveAll(requests.map {
            it.toUser(LocalDateTime.now())
        }.toList())
    }
}

fun UserCreateRequest.toUser(joinedAt: LocalDateTime) = User(
    id = id,
    name = name,
    joinedAt = joinedAt
)