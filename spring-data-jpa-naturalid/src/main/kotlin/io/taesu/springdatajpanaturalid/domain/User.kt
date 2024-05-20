package io.taesu.springdatajpanaturalid.domain

import jakarta.persistence.*
import org.hibernate.annotations.NaturalId

/**
 * Created by itaesu on 2024/05/20.
 *
 * @author Lee Tae Su
 * @version spring-data-jpa-naturalid
 * @since spring-data-jpa-naturalid
 */
@Table(name = "app_user")
@Entity
class User(
    @NaturalId
    @Column(name = "email", unique = true, nullable = false)
    val email: String,

    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "intro")
    val intro: String? = null,

    @Id
    @Column(name = "user_key", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 1)
    val key: Long = 0L,
) {
    override fun equals(other: Any?): Boolean =
        this === other || (
            other is User
                && other.email == email
            )


    override fun hashCode(): Int {
        return email.hashCode()
    }
}
