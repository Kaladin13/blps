package ru.itmo.blps.model

data class User(
    val username: String,
    val password: String,
    val roles: List<Role>,
)
