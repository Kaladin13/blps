package ru.itmo.blps.dao.impl

import org.springframework.stereotype.Repository
import ru.itmo.blps.dao.UserDao
import ru.itmo.blps.model.Role
import ru.itmo.blps.model.User

@Repository
class UserDaoImpl : UserDao {

    override fun getByUsername(username: String): User? {
//        val users = XmlMapper().readValue(stream, object : TypeReference<List<User>>() {}) ?: emptyList()
//        return users[0]
        val rolesList = listOf(
            Role(1, "MAX")
        )

        return User("max", "secret1", rolesList)
    }

    companion object {
        private val stream = UserDaoImpl::class.java.getResource("security/users.xml")
    }
}
