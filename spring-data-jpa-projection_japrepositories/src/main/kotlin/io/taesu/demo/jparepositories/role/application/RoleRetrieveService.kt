package io.taesu.demo.jparepositories.role.application

import io.taesu.demo.jparepositories.role.domain.Role
import io.taesu.demo.jparepositories.role.domain.RoleRepository
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
@Transactional(readOnly = true)
class RoleRetrieveService(private val roleRepository: RoleRepository) {
    fun retrieve(keys: Collection<Long>): List<Role> {
        return roleRepository.findAllById(keys)
    }
}