package io.taesu.demo.jparepositories.user.application

import io.taesu.demo.jparepositories.user.domain.User
import io.taesu.demo.jparepositories.user.domain.UserRepository
import org.springframework.stereotype.Service

/**
 * Created by itaesu on 2021/06/30.
 *
 * @author Lee Tae Su
 * @version TBD
 * @since TBD
 */
@Service
class UserRetrieveService(private val userRepository: UserRepository) {
    fun retrieveByRole(roleId: String): List<User> {
        return userRepository.findAllByUserRolesRoleId(roleId)
    }
}