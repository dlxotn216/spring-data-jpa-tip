package io.taesu.demo.jparepositories.user.interfaces

import org.springframework.beans.factory.annotation.Value

/**
 * Created by itaesu on 2021/06/30.
 *
 * @author Lee Tae Su
 * @version TBD
 * @since TBD
 */
interface UserKeyIdRoleIdDto {
    fun getKey(): Long
    fun getId(): String
    // fun getUserRoles(): List<UserRoleDto>

    // @JvmDefault <arg>-Xjvm-default=enable</arg> 활성화 필요
    // fun getFullName(): String {
    //     return "${getKey()} ${getId()}"
    // }

    @Value("#{args[0] + ' ' + target.id + '!'}")
    fun getSalutation(prefix: String): String


    // Aggregate root가 아닌 경우는 안되는듯.
    interface UserRoleDto {
        fun getRole(): RoleDto

        @Value("#{target.role.id}")
        fun getRoleId(): String

        interface RoleDto {
            fun getId(): String
        }
    }
}