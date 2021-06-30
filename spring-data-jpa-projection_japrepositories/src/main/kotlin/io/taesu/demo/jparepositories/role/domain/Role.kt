package io.taesu.demo.jparepositories.role.domain

import io.taesu.demo.jparepositories.user.domain.User
import org.hibernate.annotations.NaturalId
import org.springframework.data.jpa.repository.JpaRepository
import javax.persistence.*

/**
 * Created by itaesu on 2021/06/30.
 *
 * @author Lee Tae Su
 * @version TBD
 * @since TBD
 */
@Entity
@Table(name = "MST_ROLE")
class Role(
    @Id
    @Column(name = "ROLE_KEY")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROLE_SEQ")
    @SequenceGenerator(name = "ROLE_SEQ", sequenceName = "ROLE_SEQ", initialValue = 1, allocationSize = 1)
    val key: Long = 0L,

    @NaturalId
    @Column(name = "ID", unique = true, updatable = false, nullable = false, length = 256)
    val id: String,

    @Column(name = "ROLE_NAME", nullable = false, length = 256)
    val name: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Role
        if (id == other.id) return true

        return false
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

interface RoleRepository: JpaRepository<Role, Long>