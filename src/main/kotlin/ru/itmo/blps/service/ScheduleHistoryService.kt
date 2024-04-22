package ru.itmo.blps.service;

import ru.itmo.blps.model.Schedule
import ru.itmo.blps.model.ScheduleStatus

interface ScheduleHistoryService {
    fun add(prevStatus: ScheduleStatus? = null, schedule: Schedule)
}
