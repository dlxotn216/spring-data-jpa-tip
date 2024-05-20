package io.taesu.springdatajpanaturalid.application

import io.taesu.springdatajpanaturalid.domain.User
import io.taesu.springdatajpanaturalid.domain.UserRepository
import jakarta.persistence.EntityManager
import jakarta.persistence.EntityManagerFactory
import jakarta.persistence.PersistenceContext
import org.assertj.core.api.Assertions.assertThat
import org.hibernate.SessionFactory
import org.hibernate.stat.Statistics
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest


/**
 * Created by itaesu on 2024/05/20.
 *
 * @author Lee Tae Su
 * @version spring-data-jpa-naturalid
 * @since spring-data-jpa-naturalid
 */
@SpringBootTest
class UserServiceTest {
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
    fun `ID로 조회 시 영속성 컨텍스트에 로드된 Entity를 반환한다`() {
        // given
        val userKey = userRepository.save(
            User(
                email = "taesulee93@gmail.com",
                name = "Tae Su, Lee",
            )
        ).key
        statistics!!.clear()

        // when
        val result = userService.retrieve(userKey)

        // then
        assertThat(result.loadedPersistenceContext).isTrue()
        assertThat(statistics!!.prepareStatementCount).isEqualTo(1L)
    }

    @Test
    fun `NaturalId로 조회 시 영속성 컨텍스트에 로드된 Entity를 반환한다`() {
        // given
        userRepository.save(
            User(
                email = "taesulee93@gmail.com",
                name = "Tae Su, Lee",
            )
        )
        statistics!!.clear()

        // when
        val result = userService.retrieve("taesulee93@gmail.com")

        // then
        assertThat(result.loadedPersistenceContext).isTrue()
        assertThat(statistics!!.prepareStatementCount).isEqualTo(101L)
    }

    @Test
    fun `Enhanced NaturalId로 조회 시 영속성 컨텍스트에 로드된 Entity를 반환한다`() {
        // given
        userRepository.save(
            User(
                email = "taesulee93@gmail.com",
                name = "Tae Su, Lee",
            )
        )
        statistics!!.clear()

        // when
        val result = userService.retrieveEnhanced("taesulee93@gmail.com")

        // then
        assertThat(result.loadedPersistenceContext).isTrue()
        assertThat(statistics!!.prepareStatementCount).isEqualTo(1L)
    }

    @AfterEach
    fun tearDown() {
        userRepository.deleteAll()
    }
}
