package io.taesu.demo.jparepositories

import io.taesu.demo.jparepositories.role.application.RoleCreateService
import io.taesu.demo.jparepositories.role.interfaces.RoleCreateRequest
import io.taesu.demo.jparepositories.user.application.UserCreateService
import io.taesu.demo.jparepositories.user.application.UserRetrieveService
import io.taesu.demo.jparepositories.user.domain.User
import io.taesu.demo.jparepositories.user.domain.UserRepository
import io.taesu.demo.jparepositories.user.domain.UserRoleRepository
import io.taesu.demo.jparepositories.user.interfaces.UserCreateRequest
import io.taesu.demo.jparepositories.user.interfaces.UserKeyIdDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.test.context.ActiveProfiles
import javax.persistence.EntityManager

@ActiveProfiles("test")
@SpringBootTest
class JparepositoriesApplicationTests {
    @Autowired
    private lateinit var roleCreateService: RoleCreateService

    @Autowired
    private lateinit var userCreateService: UserCreateService

    @Autowired
    private lateinit var userRetrieveService: UserRetrieveService

    @Autowired
    private lateinit var userRoleRepository: UserRoleRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var em: EntityManager

    @Test
    fun shouldJoinAllGraph() {
        val roleAdmin = roleCreateService.create(
            RoleCreateRequest(
                id = "ADMIN",
                name = "Admin"
            )
        )
        val roleUser = roleCreateService.create(
            RoleCreateRequest(
                id = "USER",
                name = "User"
            )
        )

        userCreateService.create(
            UserCreateRequest(
                id = "taesu",
                name = "Lee TAeSu",
                roleKeys = setOf(roleAdmin.key, roleUser.key)
            )
        )

        userCreateService.create(
            UserCreateRequest(
                id = "lee",
                name = "Lee",
                roleKeys = setOf(roleAdmin.key)
            )
        )

        userCreateService.create(
            UserCreateRequest(
                id = "kim",
                name = "Kim",
                roleKeys = setOf(roleUser.key)
            )
        )

        val resultList = em.createQuery(
            """
            select u from User u
            join u.userRoles ur
            join ur.role r
            where r.id =:id
        """.trimIndent()
        ).setParameter("id", "ADMIN").resultList

        val admins = userRetrieveService.retrieveByRole("ADMIN")

        userRoleRepository.findAllByRoleId("ADMIN")
        assertThat(admins).hasSize(2)
        assertThat(resultList).hasSize(2)
    }

    @Test
    fun queryTestSliceAndPage() {
        (1..1000).map {
            UserCreateRequest(
                id = "taesu$it",
                name = "Lee TAeSu",
                roleKeys = emptySet()
            )
        }.toList()
            .apply {
                userCreateService.create(this)
            }

        val findAllByIdLike: Slice<User> = userRepository.findAllByIdLike("taesu%", Pageable.ofSize(100))
        val allByIdLike: Page<User> = userRepository.getAllByIdLike("taesu%", Pageable.ofSize(100))

        assertThat(findAllByIdLike.size).isEqualTo(allByIdLike.size)
        assertThat(allByIdLike.totalElements).isEqualTo(1000)
    }

    @Test
    fun queryTestSliceFetchAll() {
        (1..1000).map {
            UserCreateRequest(
                id = "taesu$it",
                name = "Lee TAeSu",
                roleKeys = emptySet()
            )
        }.toList()
            .apply {
                userCreateService.create(this)
            }

        val result = mutableSetOf<User>()
        var fetch = userRepository.findAllByIdLike("taesu%", Pageable.ofSize(10))
        while (!fetch.isLast) {
            result.addAll(fetch.content)
            fetch = userRepository.findAllByIdLike("taesu%", fetch.nextOrLastPageable())
        }
        if (fetch.hasContent()) {
            result.addAll(fetch.content)
        }

        assertThat(result.size).isEqualTo(1000)
    }

    @Test
    fun queryTestToDtoProjection() {
        val roleAdmin = roleCreateService.create(
            RoleCreateRequest(
                id = "ADMIN",
                name = "Admin"
            )
        )
        val roleUser = roleCreateService.create(
            RoleCreateRequest(
                id = "USER",
                name = "User"
            )
        )

        userCreateService.create(
            UserCreateRequest(
                id = "taesu",
                name = "Lee TAeSu",
                roleKeys = setOf(roleAdmin.key, roleUser.key)
            )
        )

        userCreateService.create(
            UserCreateRequest(
                id = "lee",
                name = "Lee",
                roleKeys = setOf(roleAdmin.key)
            )
        )

        userCreateService.create(
            UserCreateRequest(
                id = "kim",
                name = "Kim",
                roleKeys = setOf(roleUser.key)
            )
        )

        val findUserKeyIdDtoByUserRolesRoleIdTo = userRepository.findUserKeyIdDtoByUserRolesRoleId("ADMIN")
        val findAnyByUserRolesRoleId = userRepository.findAnyByUserRolesRoleId("ADMIN", UserKeyIdDto::class.java)
        val findUserKeyIdRoleIdDtoByUserRolesRoleId = userRepository.findUserKeyIdRoleIdDtoByUserRolesRoleId("ADMIN")

        assertThat(findUserKeyIdDtoByUserRolesRoleIdTo).hasSize(2)
        assertThat(findAnyByUserRolesRoleId).hasSize(2)
        assertThat(findUserKeyIdRoleIdDtoByUserRolesRoleId).hasSize(2)

        assertThat(findUserKeyIdRoleIdDtoByUserRolesRoleId[0].getSalutation("test")).isEqualTo("test taesu!")
    }

}
