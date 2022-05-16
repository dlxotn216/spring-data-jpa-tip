package me.taesu.springdatajpaquerydsltip.interfaces

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.domain.PageRequest

/**
 * Created by itaesu on 2022/05/16.
 *
 * @author Lee Tae Su
 * @version ConsentV3 v1.0 wB202203
 * @since ConsentV3 v1.0 wB202203
 */
open class PageableCriteria(
    page: Int = 1,
    size: Int = 10,
) {
    private val page = if (page < 1) 1 else page
    private val size = if (size > 100) 100 else size
    val pageRequest = PageRequest.of(this.page - 1, this.size)
}

class IssuePaginateCriteria(
    page: Int,
    size: Int,
    val id: String? = null,
    val name: String? = null,
    val status: String? = null,
    val description: String? = null,
): PageableCriteria(page, size)