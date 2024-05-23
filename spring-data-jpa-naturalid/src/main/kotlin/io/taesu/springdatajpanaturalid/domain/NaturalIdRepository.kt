package io.taesu.springdatajpanaturalid.domain

import jakarta.persistence.EntityManager
import jakarta.persistence.EntityNotFoundException
import org.hibernate.Session
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.support.JpaEntityInformation
import org.springframework.data.jpa.repository.support.SimpleJpaRepository
import org.springframework.data.repository.NoRepositoryBean
import java.io.Serializable


/**
 * Created by itaesu on 2024/05/21.
 *
 * @author Lee Tae Su
 * @version spring-data-jpa-naturalid
 * @since spring-data-jpa-naturalid
 */
@NoRepositoryBean
interface NaturalIdRepository<T, ID, NID>: JpaRepository<T, ID> {
    fun naturalId(naturalId: NID): T
}

class NaturalIdRepositoryImpl<T, ID: Serializable, NID: Serializable>(
    entityInformation: JpaEntityInformation<T, *>,
    private val entityManager: EntityManager,
): SimpleJpaRepository<T, ID>(entityInformation, entityManager), NaturalIdRepository<T, ID, NID> {
    override fun naturalId(naturalId: NID): T {
        return entityManager.unwrap(Session::class.java)
            .bySimpleNaturalId(this.domainClass)
            .load(naturalId) ?: throw EntityNotFoundException("Entity not found by: $naturalId")

    }
}
