package ru.itmo.blps.security

data class CustomBearerUser(
    val userId: Long,
    val roles: List<String>? = null,
    val username: String? = null,
)