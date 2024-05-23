package io.taesu.springdatajpanaturalid.config

import io.taesu.springdatajpanaturalid.domain.NaturalIdRepositoryImpl
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

/**
 * Created by itaesu on 2024/05/21.
 *
 * @author Lee Tae Su
 * @version spring-data-jpa-naturalid
 * @since spring-data-jpa-naturalid
 */
@Configuration
@EnableJpaRepositories(
    basePackages = ["io.taesu.springdatajpanaturalid"],
    repositoryBaseClass = NaturalIdRepositoryImpl::class
)
class PersistenceConfig {
}
