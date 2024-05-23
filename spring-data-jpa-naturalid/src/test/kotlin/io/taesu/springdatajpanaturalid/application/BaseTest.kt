package io.taesu.springdatajpanaturalid.application

import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import jakarta.persistence.Table
import org.junit.jupiter.api.AfterEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

/**
 * Created by itaesu on 2024/05/21.
 *
 * @author Lee Tae Su
 * @version spring-data-jpa-naturalid
 * @since spring-data-jpa-naturalid
 */
@SpringBootTest
class BaseTest {
    @Autowired
    private lateinit var cleanUp: CleanupDatabase

    @AfterEach
    fun tearDown() {
        cleanUp.all()
    }
}

@Component
class CleanupDatabase(
    private var jdbcTemplate: JdbcTemplate,
    @PersistenceContext
    private var entityManager: EntityManager,
) {

    @Transactional
    fun all() {
        val tables = entityManager.metamodel
            .entities
            .filter { it.javaType.getAnnotation(Table::class.java) != null }
            .map { it.javaType.getAnnotation(Table::class.java).name }

        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE")
        tables.forEach { table -> jdbcTemplate.execute("TRUNCATE TABLE $table") }
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE")
    }
}
