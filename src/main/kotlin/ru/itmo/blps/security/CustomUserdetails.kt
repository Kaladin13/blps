package ru.itmo.blps.security

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import ru.itmo.blps.model.User

class CustomUserDetails(private val user: User) : UserDetails {

    override fun getAuthorities() = user.roles.map {
        SimpleGrantedAuthority(it.name)
    }

    override fun getPassword() = user.password

    override fun getUsername() = user.username

    override fun isAccountNonExpired() = true
    override fun isAccountNonLocked() = true
    override fun isCredentialsNonExpired() = true
    override fun isEnabled() = true
}