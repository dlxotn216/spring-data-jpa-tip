package io.taesu.custombaserepository.domain

import jakarta.persistence.MappedSuperclass

/**
 * Created by itaesu on 2024/05/21.
 *
 * @author Lee Tae Su
 * @version custombaserepository
 * @since custombaserepository
 */
@MappedSuperclass
abstract class BaseEntity(
    var deleted: Boolean
)
