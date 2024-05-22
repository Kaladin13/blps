//package ru.itmo.blps.exception
//
//import org.springframework.http.ResponseEntity
//import org.springframework.web.bind.annotation.ControllerAdvice
//import org.springframework.web.bind.annotation.ExceptionHandler
//import org.springframework.web.server.ResponseStatusException
//import ru.itmo.blps.generated.model.ErrorResponse
//
//@ControllerAdvice
//class GlobalExceptionHandler {
//    @ExceptionHandler(ResponseStatusException::class)
//    fun handleResponseStatusException(e: ResponseStatusException): ResponseEntity<ErrorResponse> {
//
//        return ResponseEntity.status(e.statusCode)
//                .body(
//                        ErrorResponse()
//                                .status(e.statusCode.toString())
//                                .reason(e.reason ?: "Unexpected error occurred")
//                )
//    }
//
//}