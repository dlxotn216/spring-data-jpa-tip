package io.taesu.springdatajpanaturalid.domain

import jakarta.persistence.*
import org.hibernate.Hibernate
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.LazyCollection
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
@DynamicUpdate
class User(
    @NaturalId
    @Column(name = "email", unique = true, nullable = false, updatable = false)
    val email: String,

    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "intro")
    var intro: String? = null,

    @Id
    @Column(name = "user_key", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 1)
    val key: Long = 0L,
) {

    @OneToMany(
        fetch = FetchType.LAZY,
        cascade = [CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH],
        mappedBy = "user"
    )
    // @LazyCollection(value = org.hibernate.annotations.LazyCollectionOption.EXTRA)
    // @OrderColumn(name = "seq_no")
    private val _tickets: MutableList<Ticket> = mutableListOf()
    val tickets: List<Ticket>
        get() = _tickets

    // LazyCollectionOption.EXTRA
    // select max(seq_no) + 1 from app_ticket where user_key=?
    // val ticketsSize get() = _tickets.size

    // select count(ticket_key) from app_ticket where user_key=?
    val ticketsSize get() = Hibernate.size(_tickets)

    // OrderColumn의 동작
    // insert into app_ticket (seq_no,subject,user_key,ticket_key) values (?,?,?,?)
    //
    // select max(seq_no) + 1 from app_ticket where user_key=?
    // update app_ticket set seq_no=? where ticket_key=?
    fun addTicket(subject: String): Ticket {
        return Ticket(subject, this, 0).also { _tickets.add(it) }
    }

    fun containsTicket(ticket: Ticket): Boolean {
        // LazyCollectionOption.EXTRA
        // select 1 from app_ticket where user_key=? and ticket_key=?
        // return _tickets.contains(ticket)


        // select 1 from app_ticket where user_key=? and ticket_key=?
        return Hibernate.contains(_tickets, ticket)
    }

    fun getTicket(index: Int): Ticket {
        // LazyCollectionOption.EXTRA
        // select t1_0.ticket_key,t1_0.seq_no,t1_0.subject,t1_0.user_key from app_ticket t1_0 where t1_0.user_key=? and t1_0.seq_no=?
        // return _tickets[index]

        // select t1_0.ticket_key,t1_0.seq_no,t1_0.subject,t1_0.user_key from app_ticket t1_0 where t1_0.user_key=? and t1_0.seq_no=?
        return Hibernate.get(_tickets, index)
    }

    // select t1_0.ticket_key,t1_0.seq_no,t1_0.subject,t1_0.user_key from app_ticket t1_0 where t1_0.user_key=? and t1_0.seq_no=?
    fun getAll(): List<Ticket> {
        return tickets
    }

    override fun equals(other: Any?): Boolean =
        this === other || (
            other is User
                && other.email == email
            )


    override fun hashCode(): Int {
        return email.hashCode()
    }
}
