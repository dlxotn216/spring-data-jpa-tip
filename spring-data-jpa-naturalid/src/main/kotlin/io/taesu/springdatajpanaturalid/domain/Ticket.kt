package io.taesu.springdatajpanaturalid.domain

import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

/**
 * Created by itaesu on 2024/05/21.
 *
 * @author Lee Tae Su
 * @version spring-data-jpa-naturalid
 * @since spring-data-jpa-naturalid
 */
@Table(name = "app_ticket")
@Entity
class Ticket(
    @Column(name = "subject")
    val subject: String,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_key", nullable = false)
    val user: User,

    @Column(name = "seq_no")
    val order: Int = 0,

    @Id
    @Column(name = "ticket_key", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "ticket_seq", sequenceName = "ticket_seq", allocationSize = 1)
    val key: Long = 0L,
)

interface TicketRepository : JpaRepository<Ticket, Long> {

}
