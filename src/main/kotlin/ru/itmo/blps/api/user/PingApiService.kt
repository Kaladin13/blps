package ru.itmo.blps.api.user

import lombok.RequiredArgsConstructor
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import ru.itmo.blps.generated.api.user.PingApiDelegate
import ru.itmo.blps.generated.model.PingResponse

@Component
@RequiredArgsConstructor
class PingApiService : PingApiDelegate {

    override fun ping(): ResponseEntity<PingResponse> {
        return ResponseEntity(PingResponse().pong("xxx"), HttpStatus.OK)
    }
}