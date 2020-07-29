package com.qagile.qevent.api.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.qagile.qevent.api.configuration.rabbitmq.entities.RabbitConstants
import com.qagile.qevent.api.configuration.rabbitmq.entities.UserAcquirer
import io.reactivex.Single
import java.lang.Exception
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MessageService {

    private val logger = LoggerFactory.getLogger(MessageService::class.java)

    private val mapper = ObjectMapper().registerModule(KotlinModule())

    @Autowired
    private lateinit var rabbitTemplate: RabbitTemplate

    fun sendMessage(message: UserAcquirer): Single<Boolean> {
        logger.info("MessageService - sendMessage, message: $message by topic: ${RabbitConstants.QEVENT_ACQUIRER_QUEUE}")

        return Single.fromCallable {
            try {
                val data = mapper.writeValueAsString(message)
                rabbitTemplate.convertAndSend(RabbitConstants.QEVENT_ACQUIRER_EX, RabbitConstants.QEVENT_ACQUIRER_ROUTING_KEY, data)
                true
            } catch (exception: Exception) {
                logger.error("MessageService - Error sendMessage, message: $message by topic: ${RabbitConstants.QEVENT_ACQUIRER_QUEUE}, error: $exception")
                false
            }
        }
    }
}
