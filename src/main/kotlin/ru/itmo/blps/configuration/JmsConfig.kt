package ru.itmo.blps.configuration

import jakarta.jms.ConnectionFactory
import org.apache.qpid.jms.JmsConnectionFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jms.annotation.EnableJms
import org.springframework.jms.core.JmsTemplate
import org.springframework.jms.support.converter.MappingJackson2MessageConverter
import org.springframework.jms.support.converter.MessageType


@Configuration
@EnableJms
class JmsConfig {
    @Bean
    fun jmsConnectionFactory(
            @Value("\${jms_url}") jmsUrl: String
    ): ConnectionFactory {
        return JmsConnectionFactory(jmsUrl)
    }

    @Bean
    fun jacksonJmsMessageConverter(): MappingJackson2MessageConverter {
        val converter = MappingJackson2MessageConverter()
        converter.setTargetType(MessageType.TEXT)
        converter.setTypeIdPropertyName("_type")
        return converter
    }

    @Bean
    fun jmsTemplate(
            connectionFactory: ConnectionFactory,
            jacksonJmsMessageConverter: MappingJackson2MessageConverter
    ): JmsTemplate {
        val jmsTemplate = JmsTemplate(connectionFactory)
        jmsTemplate.messageConverter = jacksonJmsMessageConverter
        return jmsTemplate
    }


}