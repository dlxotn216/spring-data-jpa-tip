package me.taesu.springdatajpaquerydsltip.infra

import org.springframework.data.domain.Page

/**
 * Created by itaesu on 2022/07/14.
 *
 * @author Lee Tae Su
 * @version spring-data-jpa-querydsl-tip
 * @since spring-data-jpa-querydsl-tip
 */
interface MemberQuery {
    fun findAllByPaginate(criteria: MemberPaginateCriteria): Page<MemberQueryResult>
}