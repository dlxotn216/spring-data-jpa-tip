package me.taesu.springdatajpaquerydsltip.infra.impl

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.core.types.dsl.SetPath
import com.querydsl.core.types.dsl.StringPath
import me.taesu.springdatajpaquerydsltip.domain.Member
import me.taesu.springdatajpaquerydsltip.domain.QMember
import me.taesu.springdatajpaquerydsltip.infra.MemberPaginateCriteria
import me.taesu.springdatajpaquerydsltip.infra.MemberQuery
import me.taesu.springdatajpaquerydsltip.infra.MemberQueryResult
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

/**
 * Created by itaesu on 2022/07/14.
 *
 * @author Lee Tae Su
 * @version spring-data-jpa-querydsl-tip
 * @since spring-data-jpa-querydsl-tip
 */
class MemberQueryImpl: MemberQuery, QuerydslRepositorySupport(Member::class.java) {
    override fun findAllByPaginate(criteria: MemberPaginateCriteria): Page<MemberQueryResult> {
        val queryDsl = querydsl!!
        val mb = QMember("mb")

        val predicate =
            getPredicates(criteria, mb).let {
                if (it.isEmpty()) null else it.reduce { acc, expression -> acc.and(expression) }
            }
        val pageRequest = criteria.pageRequest

        val query = queryDsl.createQuery<MemberQueryResult>()
            .select(MemberQueryResult.projection(mb))
            .from(mb)
            .where(predicate)
            .limit(pageRequest.pageSize.toLong())
            .offset(pageRequest.offset)
            .orderBy(mb.key.desc())

        val count = query.fetchCount()
        return if (count == 0L) {
            PageImpl(emptyList(), pageRequest, count)
        } else {
            PageImpl(query.fetch(), pageRequest, count)
        }
    }

    internal fun getPredicates(
        criteria: MemberPaginateCriteria,
        mb: QMember,
    ): List<BooleanExpression> {
        return listOfNotNull(
            criteria.name?.let {
                mb.name.containsIgnoreCase(it)
            },
            criteria.hobby?.let {
                // mb.hobbies.contains(it),
                // Expressions.booleanTemplate("lower({0}) like concat('%', {1}, '%')", mb.hobbies, it.lowercase()),
                mb.hobbies.containsIgnoreCase(it)
            },
        )
    }
}

private fun SetPath<String, StringPath>.containsIgnoreCase(value: String) =
    Expressions.booleanTemplate("lower({0}) like concat('%', {1}, '%')", this, value.lowercase())