package ru.itmo.blps.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.Serializable
import java.security.Key
import java.util.*


@Service
class JwtTokenUtil(
    @Value("\${jwt.access.secret}") private val accessTokenSecret: String,
) : Serializable {
    private fun generateToken(userId: Long, secret: String, validity: Int): String {
        val currentDate = Date()

        return Jwts.builder()
            .setSubject(userId.toString())
            .setIssuedAt(currentDate)
            .setExpiration(Date(currentDate.time + validity))
            .signWith(getSignInKey())
            .compact()
    }

    fun getIdFromToken(token: String): Long? {
        val parsedToken = Jwts.parserBuilder()
            .setSigningKey(getSignInKey())
            .build()
            .parseClaimsJws(token)
            .body;

        return parsedToken.subject.toLong();
    }

    private fun getSignInKey(): Key {
        val keyBytes = Decoders.BASE64.decode(accessTokenSecret)
        return Keys.hmacShaKeyFor(keyBytes)
    }

}
