package ru.itmo.blps.dao

import ru.itmo.blps.model.Program
import ru.itmo.blps.model.Schedule
import java.time.OffsetDateTime

interface UserScheduleDao {

    fun insert(schedule: Schedule)
    fun getScheduleForDay(startTime: OffsetDateTime): List<Program>
}