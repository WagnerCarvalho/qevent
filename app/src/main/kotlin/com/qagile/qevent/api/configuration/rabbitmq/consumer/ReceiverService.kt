package com.qagile.qevent.api.configuration.rabbitmq.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.qagile.qevent.api.configuration.rabbitmq.QueueMessage
import com.qagile.qevent.api.configuration.rabbitmq.entities.RabbitConstants
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service

@Service
class ReceiverService {
    private val mapper = ObjectMapper().registerModule(KotlinModule())
    private val logger = LoggerFactory.getLogger(ReceiverService::class.java)

    @RabbitListener(queues = arrayOf(RabbitConstants.QUSER_OWNER_EVENT_QUEUE))
    fun getMessage(message: String) {
        try {
            val event: QueueMessage = mapper.readValue(message)
            logger.info("ReceiverService - getMessage message: $event")
        } catch (e: Exception) {
            logger.error("ReceiverService - getMessage error: ${e.message}")
        }
    }
}
