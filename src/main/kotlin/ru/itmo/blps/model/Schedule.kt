package ru.itmo.blps.model

import java.time.OffsetDateTime

data class Schedule(
    val id: Long? = null,
    val status: ScheduleStatus,
    val programs: List<Program>,
    val date: OffsetDateTime,
)