package me.taesu.springdatajpaquerydsltip.infra

import me.taesu.springdatajpaquerydsltip.infra.impl.IssuePaginateCriteria
import me.taesu.springdatajpaquerydsltip.infra.impl.IssueQueryResult
import org.springframework.data.domain.Page

/**
 * Created by itaesu on 2022/05/16.
 *
 * @author Lee Tae Su
 * @version ConsentV3 v1.0 wB202203
 * @since ConsentV3 v1.0 wB202203
 */
interface IssueQuery {
    fun findAllByPaginate(criteria: IssuePaginateCriteria): Page<IssueQueryResult>
}