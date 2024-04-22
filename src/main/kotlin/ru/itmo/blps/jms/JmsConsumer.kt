package ru.itmo.blps.jms

import jakarta.jms.Session
import org.slf4j.LoggerFactory
import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Component

@Component
class JmsConsumer {

    @JmsListener(destination = "\${topic_name}", containerFactory = "jmsFactory")
    fun receiveMessage(
            message: String, session: Session
    ) {
        try {
            logger.info("Received message: {}", message)
            session.commit()
        } catch (e: Exception) {
            logger.error("Error while reading jms", e)
            session.rollback()
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(JmsConsumer::class.java)
    }
}
