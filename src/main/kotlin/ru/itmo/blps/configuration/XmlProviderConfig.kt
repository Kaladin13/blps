package ru.itmo.blps.configuration

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import org.springframework.stereotype.Component
import ru.itmo.blps.model.User
import java.io.InputStream


@Component
class XmlProviderConfig {

    fun getUsersProvider(): List<User>? {
        val stream = XmlProviderConfig::class.java.getResource("security/users.xml")

        return XmlMapper().readValue(stream, TypeReference<List<User>>)
    }
}