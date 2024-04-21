package ru.itmo.blps.jms

import org.springframework.jms.core.JmsTemplate
import org.springframework.stereotype.Service
import ru.itmo.blps.model.ScheduleHistory

@Service
class JmsService(
        private val jmsTemplate: JmsTemplate
) {
    fun sendMessage(destination: String, message: ScheduleHistory) {
        jmsTemplate.convertAndSend(destination, message)
    }
}
