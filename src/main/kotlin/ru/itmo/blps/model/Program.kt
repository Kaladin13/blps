package ru.itmo.blps.model

import java.time.OffsetDateTime

data class Program(
    val id: Long? = null,
    val name: String,
    val startTime: OffsetDateTime,
    val endTime: OffsetDateTime,
)
