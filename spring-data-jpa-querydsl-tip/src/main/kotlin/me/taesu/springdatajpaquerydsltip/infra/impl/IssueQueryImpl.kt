package me.taesu.springdatajpaquerydsltip.infra.impl

import com.querydsl.core.types.dsl.BooleanExpression
import me.taesu.springdatajpaquerydsltip.domain.Issue
import me.taesu.springdatajpaquerydsltip.domain.QIssue
import me.taesu.springdatajpaquerydsltip.infra.IssueQuery
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport


class IssueQueryImpl: IssueQuery, QuerydslRepositorySupport(Issue::class.java) {
    override fun findAllByPaginate(criteria: IssuePaginateCriteria): Page<IssueQueryResult> {
        val queryDsl = querydsl!!
        val iu = QIssue("iu")

        val predicate =
            getPredicates(criteria, iu).let {
                if (it.isEmpty()) null else it.reduce { acc, expression -> acc.and(expression) }
            }
        val pageRequest = criteria.pageRequest

        val query = queryDsl.createQuery<IssueQueryResult>()
            .select(IssueQueryResult.projection(iu))
            .from(iu)
            .where(predicate)
            .limit(pageRequest.pageSize.toLong())
            .offset(pageRequest.offset)
            .orderBy(iu.key.desc())

        val count = query.fetchCount()
        return if (count == 0L) {
            PageImpl(emptyList(), pageRequest, count)
        } else {
            PageImpl(query.fetch(), pageRequest, count)
        }
    }

    internal fun getPredicates(
        criteria: IssuePaginateCriteria,
        iu: QIssue,
    ): List<BooleanExpression> {
        return listOfNotNull(
            criteria.id?.let { iu.id.eq(it) },
            criteria.name?.let { iu.name.containsIgnoreCase(it) },
            criteria.status?.let { iu.status.likeIgnoreCase("%$it%") },
            criteria.description?.let { iu.description.likeIgnoreCase(it.likeEscape(), '\\') },
        )
    }
}

fun String.likeEscape() = "%${replace("_", "\\_").replace("%", "\\%")}%"