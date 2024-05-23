package io.taesu.springdatajpanaturalid.domain

import io.taesu.springdatajpanaturalid.application.BaseTest
import io.taesu.springdatajpanaturalid.application.UserService
import jakarta.persistence.EntityManagerFactory
import org.assertj.core.api.Assertions.assertThat
import org.hibernate.Hibernate
import org.hibernate.SessionFactory
import org.hibernate.stat.Statistics
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.util.StopWatch

/**
 * Created by itaesu on 2024/05/21.
 *
 * @author Lee Tae Su
 * @version spring-data-jpa-naturalid
 * @since spring-data-jpa-naturalid
 */
@SpringBootTest
class UserTest: BaseTest() {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var entityManagerFactory: EntityManagerFactory

    private var statistics: Statistics? = null

    @BeforeEach
    fun setUp() {
        this.statistics = entityManagerFactory.unwrap(SessionFactory::class.java).statistics
    }

    @Test
    fun `getReferenceById`() {
        // given
        val (userKey, ticketKey) = userService.save("1taesulee93@gmail.com")
        statistics!!.clear()

        // when
        val user1 = userRepository.findById(userKey).orElseThrow()
        val user2 = userRepository.getReferenceById(userKey)

        val set = mutableSetOf<User>().apply {
            add(user1)
            add(user2)
        }

        // then
        assertThat(user1).isEqualTo(user2)
        assertThat(set.size).isEqualTo(1)
    }

    @Test
    fun `ExtraLazyCollection 동작 테스트`() {
        // given
        val (userKey, ticketKey) = userService.save("1taesulee93@gmail.com")
        statistics!!.clear()

        // when
        val (size, contains, indexAt) = userService.lzayCollectionTest(userKey, ticketKey, 1)

        // then
    }

    @Test
    fun `ExtraLazyCollection Load All 테스트`() {
        // given
        val (userKey, ticketKey) = userService.save("2taesulee93@gmail.com")
        statistics!!.clear()

        // when
        userService.lzayCollectionLoadAll(userKey)

        // then
    }

    @Test
    fun `ExtraLazyCollection에 요소 추가 시 동작 테스트`() {
        // given
        val (userKey, ticketKey) = userService.save("3taesulee93@gmail.com")
        statistics!!.clear()

        // when
        userService.addTicket(userKey)

        // then
    }

    @Test
    fun `Scroll test`() {
        // given
        (1..1000).map {
            User(
                "test$it@email.com",
                "user$it"
            )
        }.let { userRepository.saveAll(it) }

        // when
        userService.updateAllWithScroll()

        // then
        userRepository.findAll()
            .forEach {
                assertThat(it.intro).isNotNull()
            }
    }

    @Test
    fun `StatelessSession test`() {
        // given
        (1..1000).map {
            User(
                "test$it@email.com",
                "user$it"
            )
        }.let { userRepository.saveAll(it) }

        // when
        userService.updateAllWithStatelessSession()

        // then
        userRepository.findAll()
            .forEach {
                assertThat(it.intro).isNotNull()
            }
    }

    @Test
    fun `Performance test`() {
        // given
        (1..1000000).map {
            User(
                "test$it@email.com",
                "user$it"
            )
        }.let { userRepository.saveAll(it) }

        // when
        val watch = StopWatch().apply {
            this.start("updateAllWithStatelessSession")
            userService.updateAllWithStatelessSession()
            this.stop()

            this.start("updateAllWithRepository")
            userService.updateAllWithRepository()
            this.stop()
        }

        // then
        println(watch.prettyPrint())

    }

    @Disabled
    @Test
    fun `Upsert test`() {
        // given
        val users = (1..1000).map {
            User(
                "test$it@email.com",
                "user$it"
            )
        }.let { userRepository.saveAll(it) }

        // when
        val merged = users + (1001..2000).map {
            User(
                "test$it@email.com",
                "user$it",
                key = it.toLong()       // key must be set for upsert
            )
        }
        merged.forEach {
            it.intro = "Hello ${it.email}"
        }
        userService.upsert(merged)

        // then
        userRepository.findAll().run {
            assertThat(this.size).isEqualTo(2000)
            forEach {
                assertThat(it.intro).isNotNull()
            }
        }

    }
}
