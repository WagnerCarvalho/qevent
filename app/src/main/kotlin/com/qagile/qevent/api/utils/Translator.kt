package com.qagile.qevent.api.utils

import java.util.Locale
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.MessageSource
import org.springframework.stereotype.Component

@Component
class Translator {
    @Value("\${spring.mvc.locale}")
    private val locale: String = "es"

    companion object {
        lateinit var MESSAGE_SOURCE: MessageSource
        lateinit var LOCALE: Locale

        fun getMessage(code: String, args: Array<Any>? = null): String {
            return MESSAGE_SOURCE.getMessage(code, args, LOCALE)
        }
    }

    @Autowired
    private fun Translator(messageSource: MessageSource) {
        MESSAGE_SOURCE = messageSource
        LOCALE = Locale(this.locale)
    }
}
