package io.taesu.custombaserepository.domain

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

/**
 * Created by itaesu on 2024/05/21.
 *
 * @author Lee Tae Su
 * @version custombaserepository
 * @since custombaserepository
 */
@SpringBootTest
class CouponTest {
    @Autowired
    private lateinit var couponRepository: CouponRepository

    @Test
    fun `Custom exception을 던진다`() {
        // given, when
        org.junit.jupiter.api.assertThrows<CustomException> {
            couponRepository.findOrThrow(1L)
        }
    }

    @Test
    fun `Custom exception을 던진다 by extension`() {
        // given, when
        org.junit.jupiter.api.assertThrows<CustomException> {
            couponRepository.findOrThrowExtension(1L)
        }
    }


    @Test
    fun `Hard delete 처리할 수 없다`() {
        // given, when
        org.junit.jupiter.api.assertThrows<UnsupportedOperationException> {
            couponRepository.deleteById(1L)
        }
    }

}
