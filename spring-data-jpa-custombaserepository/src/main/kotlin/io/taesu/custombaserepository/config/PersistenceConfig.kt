package io.taesu.custombaserepository.config

import io.taesu.custombaserepository.domain.BaseRepositoryImpl
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

/**
 * Created by itaesu on 2024/05/21.
 *
 * @author Lee Tae Su
 * @version custombaserepository
 * @since custombaserepository
 */
@Configuration
@EnableJpaRepositories(
    basePackages = ["io.taesu.custombaserepository"],
    repositoryBaseClass = BaseRepositoryImpl::class
)
class PersistenceConfig {
}

