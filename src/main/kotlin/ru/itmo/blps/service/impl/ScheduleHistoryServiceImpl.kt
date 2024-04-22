package ru.itmo.blps.service.impl

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.itmo.blps.dao.ScheduleHistoryDao
import ru.itmo.blps.jms.JmsProducer
import ru.itmo.blps.model.Schedule
import ru.itmo.blps.model.ScheduleHistory
import ru.itmo.blps.model.ScheduleStatus
import ru.itmo.blps.service.ScheduleHistoryService
import java.time.OffsetDateTime

@Service
class ScheduleHistoryServiceImpl(
        private val scheduleHistoryDao: ScheduleHistoryDao,
        private val jmsProducer: JmsProducer,
        @Value("\${topic_name}")
        private val topicName: String,
) : ScheduleHistoryService {
    override fun add(prevStatus: ScheduleStatus?, schedule: Schedule) {
        val historyLog = ScheduleHistory(
                scheduleDraftId = 1,
                prevStatus = prevStatus,
                currentStatus = schedule.status,
                date = schedule.date,
                updatedAt = OffsetDateTime.now(),
                programs = schedule.programs,
        )
        val persistedId = scheduleHistoryDao.insert(historyLog)

        jmsProducer.sendMessage(topicName, historyLog.copy(id = persistedId))
    }
}