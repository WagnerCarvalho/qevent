package com.qagile.qevent.core.health

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController()
class PingHandlerController {

    @GetMapping("/ping")
    fun health(): String {
        return "pong => qevent"
    }
}
