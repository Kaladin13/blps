package ru.itmo.blps.security

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority

class CustomBearerToken(
    private val customBearerUser: CustomBearerUser
) : AbstractAuthenticationToken(customBearerUser.roles?.map { SimpleGrantedAuthority(it) }) {

    init {
        isAuthenticated = true
    }

    override fun getCredentials(): Any? {
        return null
    }

    override fun getPrincipal(): Any {
        return customBearerUser
    }
}