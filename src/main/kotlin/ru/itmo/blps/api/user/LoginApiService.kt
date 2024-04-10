package ru.itmo.blps.api.user

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import ru.itmo.blps.generated.api.user.LoginApiDelegate
import ru.itmo.blps.security.JwtTokenUtil
import java.math.BigDecimal

@Service
class LoginApiService(
    private val jwtTokenUtil: JwtTokenUtil
): LoginApiDelegate {
    override fun security(id: BigDecimal): ResponseEntity<String> {
        return ResponseEntity.ok(jwtTokenUtil.generateToken(id.longValueExact(), 70_000))
    }
}