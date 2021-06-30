package io.taesu.demo.jparepositories.user.domain

import io.taesu.demo.jparepositories.role.domain.Role
import io.taesu.demo.jparepositories.user.interfaces.UserKeyIdDto
import io.taesu.demo.jparepositories.user.interfaces.UserKeyIdRoleIdDto
import org.hibernate.annotations.NaturalId
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime
import javax.persistence.*

/**
 * Created by itaesu on 2021/06/30.
 *
 * @author Lee Tae Su
 * @version TBD
 * @since TBD
 */
@Entity
@Table(name = "USR_USER")
class User(
    @Id
    @Column(name = "USER_KEY")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_SEQ")
    @SequenceGenerator(name = "USER_SEQ", sequenceName = "USER_SEQ", initialValue = 1, allocationSize = 1)
    val key: Long = 0L,

    @NaturalId
    @Column(name = "ID", unique = true, updatable = false, nullable = false, length = 256)
    val id: String,

    @Column(name = "USER_NAME", nullable = false, length = 256)
    val name: String,

    @Column(name = "JOINED_AT", updatable = false, nullable = false)
    val joinedAt: LocalDateTime,

    @OneToMany(
        cascade = [CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH],
        mappedBy = "user",
        fetch = FetchType.LAZY
    )
    val userRoles: MutableSet<UserRole> = mutableSetOf()
) {

    fun add(roles: List<Role>) {
        this.userRoles.addAll(roles.map { UserRole(user = this, role = it) })
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User
        if (id == other.id) return true

        return false
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

interface UserRepository: JpaRepository<User, Long> {
    fun findUserKeyIdDtoByUserRolesRoleId(roleId: String): List<UserKeyIdDto>
    fun findUserKeyIdRoleIdDtoByUserRolesRoleId(roleId: String): List<UserKeyIdRoleIdDto>
    fun <T> findAnyByUserRolesRoleId(roleId: String, type: Class<T>): List<T>

    fun findAllByUserRolesRoleId(roleId: String): List<User>

    fun findAllByIdLike(id: String, pageable: Pageable): Slice<User>
    fun getAllByIdLike(id: String, pageable: Pageable): Page<User>
}

interface UserRoleRepository: JpaRepository<UserRole, Long> {
    fun findAllByRoleId(roleId: String): List<UserRole>
}