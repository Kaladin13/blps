package ru.itmo.blps.dao

import ru.itmo.blps.model.Schedule
import ru.itmo.blps.model.ScheduleStatus

interface ScheduleDao {
    fun insert(schedule: Schedule)

    fun getAllByStatus(status: ScheduleStatus): List<Schedule>

    fun updateStatus(id: Long, newStatus: ScheduleStatus)
}