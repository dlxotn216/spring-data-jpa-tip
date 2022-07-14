package me.taesu.springdatajpaquerydsltip.domain

import me.taesu.springdatajpaquerydsltip.infra.IssueQuery
import org.hibernate.annotations.NaturalId
import org.springframework.data.jpa.repository.JpaRepository
import javax.persistence.*

/**
 * Created by itaesu on 2022/05/16.
 *
 * @author Lee Tae Su
 * @version ConsentV3 v1.0 wB202203
 * @since ConsentV3 v1.0 wB202203
 */
@Entity(name = "Issue")
@Table(name = "ISSUE")
class Issue(
    @Id
    @Column(name = "ISSUE_KEY")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val key: Long = 0L,

    @NaturalId
    @Column(name = "ID", length = 20, unique = true, nullable = false)
    val id: String,

    name: String,

    status: String,

    description: String?,
) {

    @Column(name = "NAME", length = 100, nullable = false)
    var name: String = name
        protected set

    @Column(name = "STATUS", length = 64, nullable = false)
    var status: String = status
        protected set

    @Column(name = "DESCRIPTION", length = 4000)
    var description: String? = description
        protected set
}

interface IssueRepository: JpaRepository<Issue, Long>, IssueQuery