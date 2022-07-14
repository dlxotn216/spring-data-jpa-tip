package me.taesu.springdatajpaquerydsltip.infra

import com.querydsl.core.types.ConstructorExpression
import com.querydsl.core.types.Projections
import me.taesu.springdatajpaquerydsltip.domain.QMember
import me.taesu.springdatajpaquerydsltip.interfaces.PageableCriteria

/**
 * Created by itaesu on 2022/07/14.
 *
 * @author Lee Tae Su
 * @version spring-data-jpa-querydsl-tip
 * @since spring-data-jpa-querydsl-tip
 */
class MemberPaginateCriteria(
    page: Int,
    size: Int,
    val name: String? = null,
    val hobby: String? = null,
): PageableCriteria(page, size)

class MemberQueryResult(
    val key: Long,
    val id: String,
    val name: String,
    val hobbies: Set<String>
) {
    companion object {
        fun projection(
            mb: QMember,
        ): ConstructorExpression<MemberQueryResult> {
            return Projections.constructor(
                MemberQueryResult::class.java,
                mb.key,
                mb.id,
                mb.name,
                mb.hobbies,
            )
        }
    }
}