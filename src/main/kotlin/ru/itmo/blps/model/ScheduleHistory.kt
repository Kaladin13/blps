package ru.itmo.blps.model

import java.time.OffsetDateTime

data class ScheduleHistory(
        val id: Long,
        val prevStatus: ScheduleStatus?,
        val currentStatus: ScheduleStatus,
        val programs: List<Program>,
        val date: OffsetDateTime,
        val changeTime: OffsetDateTime,
)