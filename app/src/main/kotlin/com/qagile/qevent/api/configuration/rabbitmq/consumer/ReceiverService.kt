package com.qagile.qevent.api.configuration.rabbitmq.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.qagile.qevent.api.configuration.rabbitmq.QueueMessage
import com.qagile.qevent.api.configuration.rabbitmq.entities.RabbitConstants
import com.qagile.qevent.api.domain.Event
import com.qagile.qevent.api.entities.request.UpdateEventRequest
import com.qagile.qevent.api.service.EventService
import java.util.concurrent.Future
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ReceiverService {
    private val mapper = ObjectMapper().registerModule(KotlinModule())
    private val logger = LoggerFactory.getLogger(ReceiverService::class.java)

    @Autowired
    private lateinit var eventService: EventService

    @RabbitListener(queues = arrayOf(RabbitConstants.QUSER_OWNER_EVENT_QUEUE))
    fun getMessage(message: String) {
        try {
            val event: QueueMessage = mapper.readValue(message)
            val status = when (event.statusMessage) {
                "CREATED" -> startEventAndGetLocation(event)
                else -> startEvent(event)
            }
            logger.info("ReceiverService - getMessage message: $event, update eventStatus: $status")
        } catch (e: Exception) {
            logger.error("ReceiverService - getMessage error: ${e.message}")
        }
    }

    fun startEvent(event: QueueMessage): Future<Event>? {
        logger.info("ReceiverService - startEvent")
        return eventService.updateEvent(UpdateEventRequest(id = event.eventId, eventStatus = event.statusMessage), event.userId).toFuture()
    }

    fun startEventAndGetLocation(event: QueueMessage): Future<Event>? {
        logger.info("ReceiverService - startEventAndGetLocation")
        return eventService.updateEvent(UpdateEventRequest(id = event.eventId, eventStatus = event.statusMessage), event.userId).toFuture()
    }
}
