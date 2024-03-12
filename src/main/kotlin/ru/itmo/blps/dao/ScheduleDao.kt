package ru.itmo.blps.dao

import ru.itmo.blps.model.Program
import ru.itmo.blps.model.Schedule
import ru.itmo.blps.model.ScheduleStatus
import java.time.OffsetDateTime

interface ScheduleDao {
    fun insert(schedule: Schedule)

    fun getAllByStatus(status: ScheduleStatus): List<Schedule>

    fun updateStatus(id: Long, newStatus: ScheduleStatus)

    fun exists(id: Long): Boolean

    fun getScheduleForDay(startTime: OffsetDateTime): List<Program>
}