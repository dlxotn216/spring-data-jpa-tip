package io.taesu.demo.jparepositories.user.interfaces

class UserCreateRequest(
    val id: String,
    val name: String,
    val roleKeys: Set<Long>
)