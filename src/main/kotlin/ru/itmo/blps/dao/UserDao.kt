package ru.itmo.blps.dao

import ru.itmo.blps.model.User

interface UserDao {
    fun getByUsername(username: String): User?
}