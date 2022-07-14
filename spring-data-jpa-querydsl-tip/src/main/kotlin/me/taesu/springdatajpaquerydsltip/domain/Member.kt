package me.taesu.springdatajpaquerydsltip.domain

import me.taesu.springdatajpaquerydsltip.converter.StringListConverter
import me.taesu.springdatajpaquerydsltip.infra.MemberQuery
import org.hibernate.annotations.NaturalId
import org.springframework.data.repository.CrudRepository
import javax.persistence.*

/**
 * Created by itaesu on 2022/07/14.
 *
 * @author Lee Tae Su
 * @version spring-data-jpa-querydsl-tip
 * @since spring-data-jpa-querydsl-tip
 */
@Entity(name = "Member")
@Table(name = "MEMBER")
class Member(
    @Id
    @Column(name = "MEMBER_KEY")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val key: Long = 0L,

    @NaturalId
    @Column(name = "ID", length = 128, unique = true, nullable = false)
    val id: String,

    @Column(name = "NAME", length = 256, nullable = false)
    val name: String,

    @Convert(converter = StringListConverter::class)
    @Column(name = "HOBBIES", columnDefinition = "TEXT", nullable = false)
    val hobbies: Set<String>
)

interface MemberRepository: CrudRepository<Member, Long>, MemberQuery