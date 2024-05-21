package io.taesu.custombaserepository.domain

import jakarta.persistence.*

/**
 * Created by itaesu on 2024/05/21.
 *
 * @author Lee Tae Su
 * @version custombaserepository
 * @since custombaserepository
 */
@Entity
class Coupon(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "coupon_sequence", sequenceName = "coupon_sequence", allocationSize = 1)
    val id: Long,

    deleted: Boolean,
): BaseEntity(deleted)

interface CouponRepository: BaseRepository<Coupon, Long>
