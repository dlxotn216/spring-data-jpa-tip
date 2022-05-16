package me.taesu.springdatajpaquerydsltip.infra.impl

import com.querydsl.core.types.ConstructorExpression
import com.querydsl.core.types.Projections
import me.taesu.springdatajpaquerydsltip.domain.QIssue

/**
 * Created by itaesu on 2022/05/16.
 *
 * @author Lee Tae Su
 * @version ConsentV3 v1.0 wB202203
 * @since ConsentV3 v1.0 wB202203
 */
class IssueQueryResult(
    val key: Long,
    val id: String,
    val name: String,
    val status: String,
    val description: String?,
) {
    companion object {
        fun projection(
            iu: QIssue,
        ): ConstructorExpression<IssueQueryResult> {
            return Projections.constructor(
                IssueQueryResult::class.java,
                iu.key,
                iu.id,
                iu.name,
                iu.status,
                iu.description
            )
        }
    }
}