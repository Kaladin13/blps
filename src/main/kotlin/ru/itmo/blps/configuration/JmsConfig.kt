package ru.itmo.blps.configuration

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import jakarta.jms.ConnectionFactory
import org.apache.qpid.jms.JmsConnectionFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jms.annotation.EnableJms
import org.springframework.jms.config.DefaultJmsListenerContainerFactory
import org.springframework.jms.config.JmsListenerContainerFactory
import org.springframework.jms.core.JmsTemplate
import org.springframework.jms.support.converter.MappingJackson2MessageConverter
import org.springframework.jms.support.converter.MessageType


@Configuration
@EnableJms
class JmsConfig {
    @Bean
    fun jmsFactory(connectionFactory: ConnectionFactory): JmsListenerContainerFactory<*> {
        val factory = DefaultJmsListenerContainerFactory()
        factory.setConnectionFactory(connectionFactory)

        factory.setSessionTransacted(true)
        return factory
    }

    @Bean
    fun jmsConnectionFactory(
            @Value("\${jms_url}") jmsUrl: String
    ): ConnectionFactory {
        return JmsConnectionFactory(jmsUrl).also {
            it.username = "admin"
            it.password = "activemq"
        }
    }

    @Bean
    fun jacksonJmsMessageConverter(): MappingJackson2MessageConverter {
        val converter = MappingJackson2MessageConverter()
        converter.setTargetType(MessageType.TEXT)
        converter.setTypeIdPropertyName("_class")
        val mapper = ObjectMapper()
        mapper.registerModule(JavaTimeModule())
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        converter.setObjectMapper(mapper)
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