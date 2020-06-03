package com.qagile.qevent.api.manager.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
class UserUnauthorizedException(code: String, message: String) : EventException(code, message)

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
open class EventException(code: String, message: String, status: HttpStatus = HttpStatus.BAD_REQUEST) : RuntimeException() {
    var code = code
    override var message = message
    var status = status
}
