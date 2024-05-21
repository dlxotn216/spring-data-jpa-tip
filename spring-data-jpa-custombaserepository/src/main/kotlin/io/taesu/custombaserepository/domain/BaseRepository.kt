package io.taesu.custombaserepository.domain

import jakarta.persistence.EntityManager
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.support.JpaEntityInformation
import org.springframework.data.jpa.repository.support.SimpleJpaRepository
import org.springframework.data.repository.NoRepositoryBean

/**
 * Created by itaesu on 2024/05/21.
 *
 * @author Lee Tae Su
 * @version custombaserepository
 * @since custombaserepository
 */
@NoRepositoryBean
interface BaseRepository<T: BaseEntity, ID>: JpaRepository<T, ID> {
    fun findOrThrow(id: ID & Any): T {
        return findById(id).orElseThrow {
            CustomException("Entity not found by id: $id")
        }
    }
}

fun <T, ID> JpaRepository<T, ID>.findOrThrowExtension(id: ID & Any): T {
    return findById(id).orElseThrow {
        CustomException("Entity not found by id: $id")
    }
}

class BaseRepositoryImpl<T: BaseEntity, ID>(
    entityInformation: JpaEntityInformation<T, *>,
    private val entityManager: EntityManager,
): SimpleJpaRepository<T, ID>(entityInformation, entityManager),
    BaseRepository<T, ID> {
    override fun deleteById(id: ID & Any) {
        throw UnsupportedOperationException("Not supported delete by")
    }

    override fun delete(entity: T) {
        throw UnsupportedOperationException("Not supported delete by")
    }

    override fun delete(spec: Specification<T>): Long {
        throw UnsupportedOperationException("Not supported delete by")
    }

    override fun deleteAllById(ids: MutableIterable<ID>) {
        throw UnsupportedOperationException("Not supported delete by")
    }

    override fun deleteAll(entities: MutableIterable<T>) {
        throw UnsupportedOperationException("Not supported delete by")
    }

    override fun deleteAll() {
        throw UnsupportedOperationException("Not supported delete by")
    }
}
