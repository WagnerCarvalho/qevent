package com.qagile.qevent.api.manager.exception

import com.fasterxml.jackson.databind.ObjectMapper
import com.qagile.qevent.api.manager.entities.ErrorData
import com.qagile.qevent.api.utils.Translator
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import retrofit2.adapter.rxjava2.HttpException

@Component
class ManagerException {

    private val logger = LoggerFactory.getLogger(ManagerException::class.java)

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    fun check(it: Throwable, messageByDefault: String, code: String = "", applicationUserId: Long = 0, isApplicable: Boolean = true, messageProcess: (HttpException, String, Long) -> String = ::getMessage): EventException {
        return try {

            if (it is HttpException) {
                when (it.code()) {
                    401 -> UserUnauthorizedException(it.code().toString(), messageProcess(it, code, applicationUserId))
                    else -> {
                        EventException(it.code().toString(), messageProcess(it, code, applicationUserId))
                    }
                }
            } else {
                EventException(messageByDefault, Translator.getMessage(messageByDefault))
            }
        } catch (exception: Exception) {
            logger.error("Error deserialize the exception: $it,$code,$applicationUserId")
            EventException(messageByDefault, Translator.getMessage(messageByDefault))
        }
    }

    private fun getMessage(it: HttpException, code: String, applicationUserId: Long): String {
        val error = it.response().errorBody()?.string()
        logger.info("Exception yellow $error $code,$applicationUserId")
        val errorData = objectMapper.readValue(error, ErrorData::class.java)

        return errorData.message
    }
}
