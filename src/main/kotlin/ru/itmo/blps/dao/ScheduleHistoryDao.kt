package ru.itmo.blps.dao

import ru.itmo.blps.model.ScheduleHistory

interface ScheduleHistoryDao {

    fun insert(scheduleHistory: ScheduleHistory): Long
}