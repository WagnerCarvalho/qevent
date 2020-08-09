package com.qagile.qevent.api.modules.quser.service

import com.qagile.qevent.api.modules.quser.entities.request.CreateUserEventRequest
import com.qagile.qevent.api.modules.quser.entities.response.CreateUserEventResponse
import com.qagile.qevent.api.modules.quser.gateway.Quser
import com.qagile.qevent.api.utils.getError
import io.reactivex.Single
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserEventService {

    private val logger = LoggerFactory.getLogger(UserEventService::class.java)

    @Autowired
    private lateinit var quser: Quser

    fun createUserEvent(userId: Long, request: CreateUserEventRequest, apiKey: String): Single<CreateUserEventResponse> {
        logger.info("Start createUserEvent by userId: $userId with request: $request")

        return quser.createUserEvent(getHeader(userId, apiKey), request)
            .doOnSuccess {
                logger.info("End createUserEvent by userId: $userId with request: $request with response: $it")
            }.doOnError {
                logger.error("End createUserEvent by userId: $userId with request: $request with error: ${it.getError()}")
            }
    }

    private fun getHeader(userId: Long, apiKey: String): Map<String, String> {

        return mapOf(
            "user_id" to userId.toString(),
            "apikey" to apiKey
        )
    }
}
