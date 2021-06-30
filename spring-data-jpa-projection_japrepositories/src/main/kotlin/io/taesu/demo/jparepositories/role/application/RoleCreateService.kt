package io.taesu.demo.jparepositories.role.application

import io.taesu.demo.jparepositories.role.domain.Role
import io.taesu.demo.jparepositories.role.domain.RoleRepository
import io.taesu.demo.jparepositories.role.interfaces.RoleCreateRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Created by itaesu on 2021/06/30.
 *
 * @author Lee Tae Su
 * @version TBD
 * @since TBD
 */
@Service
class RoleCreateService(private val roleRepository: RoleRepository) {
    @Transactional
    fun create(request: RoleCreateRequest): Role {
        return roleRepository.save(request.toRole())
    }
}

fun RoleCreateRequest.toRole() = Role(
    id = id,
    name = name
)