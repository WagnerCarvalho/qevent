package com.qagile.qevent.api.modules.qacquirer.service

import com.qagile.qevent.api.modules.qacquirer.gateway.Qacquirer
import com.qagile.qevent.api.utils.getError
import io.reactivex.Single
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class QacquirerService {

    private val logger = LoggerFactory.getLogger(QacquirerService::class.java)

    @Autowired
    private lateinit var qacquirer: Qacquirer

    @Value("\${apikey}")
    private lateinit var apiKey: String

    fun searchUser(eventId: String, userId: String): Single<Map<String, Any>> {
        logger.info("QacquirerService - searchUser userId: $userId")

        return qacquirer.getUserAcquirer(getHeader(eventId, userId))
            .doOnSuccess {
                logger.info("QacquirerService - searchUser userId: $userId with response: $it")
            }.doOnError {
                logger.error("QacquirerService - searchUser userId: $userId with error: ${it.getError()}")
            }
    }

    private fun getHeader(eventId: String, userId: String): Map<String, String> {

        return mapOf(
            "user_id" to userId,
            "email" to "test@test.com",
            "acquirer" to "mercadopago",
            "event_id" to eventId,
            "apikey" to apiKey
        )
    }
}
