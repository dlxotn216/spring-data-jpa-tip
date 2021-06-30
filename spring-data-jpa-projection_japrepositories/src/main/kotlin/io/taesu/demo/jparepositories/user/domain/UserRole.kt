package io.taesu.demo.jparepositories.user.domain

import io.taesu.demo.jparepositories.role.domain.Role
import javax.persistence.*

/**
 * Created by itaesu on 2021/06/30.
 *
 * @author Lee Tae Su
 * @version TBD
 * @since TBD
 */
@Entity
@Table(name = "USR_USER_ROLE")
class UserRole(
    @Id
    @Column(name = "USER_ROLE_KEY")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_ROLE_SEQ")
    @SequenceGenerator(name = "USER_ROLE_SEQ", sequenceName = "USER_ROLE_SEQ", initialValue = 1, allocationSize = 1)
    val key: Long = 0L,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "USER_KEY", nullable = false)
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ROLE_KEY", nullable = false)
    val role: Role
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserRole
        if (user == other.user && role == other.role) return true

        return false
    }

    override fun hashCode(): Int {
        var hash = user.hashCode()
        hash = 31 * hash + role.hashCode()
        return hash
    }
}