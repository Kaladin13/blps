package ru.itmo.blps.exception

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import ru.itmo.blps.generated.model.ErrorResponse

@ControllerAdvice
class GlobalErrorResponseExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(
        ResponseStatusException::class
    )
    fun handleResponseStatusException(ex: ResponseStatusException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(ex.statusCode)
            .body(
                ErrorResponse()
                    .reason(ex.reason)
            )
    }

}