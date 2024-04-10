package ru.itmo.blps.dao.impl

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import org.springframework.stereotype.Repository
import ru.itmo.blps.dao.UserDao
import ru.itmo.blps.model.Role
import ru.itmo.blps.model.User

@Repository
class UserDaoImpl : UserDao {

    override fun getByUsername(username: String): User? {
        val users = XmlMapper().readValue(stream, object : TypeReference<List<User>>() {}) ?: emptyList()
        return users.firstOrNull { it.username == username }
        val rolesList = listOf(
            Role(1, "2")
        )

        return User("max", "secret1", rolesList)
    }

    companion object {
        private val stream = UserDaoImpl::class.java.getResource("security/users.xml")
    }
}
