package ru.itmo.blps.security


import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import ru.itmo.blps.dao.UserDao

@Service
class JwtUserDetailsService(private val userDao: UserDao) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userDao.getByUsername(username) ?: throw UsernameNotFoundException("User not found")
        return CustomUserDetails(user)
    }
}