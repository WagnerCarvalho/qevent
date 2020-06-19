package com.qagile.qevent.api.controller

import com.qagile.qevent.api.domain.Event
import com.qagile.qevent.api.entities.request.CreateEventRequest
import com.qagile.qevent.api.entities.request.DeleteRequest
import com.qagile.qevent.api.entities.request.UpdateEventRequest
import com.qagile.qevent.api.entities.response.DeleteResponse
import com.qagile.qevent.api.modules.qacquirer.entities.request.CreateUserEventRequest
import com.qagile.qevent.api.modules.qacquirer.entities.response.CreateUserEventResponse
import com.qagile.qevent.api.routers.EventRouter
import com.qagile.qevent.api.routers.UserEventRouter
import com.qagile.qevent.api.service.EventService
import com.qagile.qevent.api.utils.toFutureResponse
import java.util.concurrent.Future
import javax.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
class EventController {

    @Autowired
    private lateinit var eventService: EventService

    @GetMapping(EventRouter.GET_EVENT_V1)
    fun getEvent(
        @PathVariable id: String,
        @RequestHeader(value = "user_id") userId: Long,
        @RequestHeader(value = "apikey") apiKey: String
    ): Future<Event> {
        return eventService.checkEvent(id, userId).toFutureResponse()
    }

    @PutMapping(EventRouter.UPDATE_EVENT_V1)
    fun updateEvent(
        @PathVariable id: String,
        @Valid @RequestBody updateEventRequest: UpdateEventRequest,
        @RequestHeader(value = "user_id") userId: Long,
        @RequestHeader(value = "apikey") apiKey: String
    ): Future<Event> {

        return eventService.checkUpdateEvent(updateEventRequest.get(id), userId).toFutureResponse()
    }

    @DeleteMapping(EventRouter.DELETE_EVENT_V1)
    fun removeEvent(
        @PathVariable id: String,
        @RequestHeader(value = "user_id") userId: Long,
        @RequestHeader(value = "apikey") apiKey: String
    ): Future<DeleteResponse> {

        return eventService.checkRemoveEvent(DeleteRequest(id), userId).toFutureResponse()
    }

    @PostMapping(EventRouter.CREATE_EVENT_V1)
    fun createEvent(
        @Valid @RequestBody createEventRequest: CreateEventRequest,
        @RequestHeader(value = "user_id") userId: Long,
        @RequestHeader(value = "apikey") apiKey: String
    ): Future<Event> {

        return eventService.checkCreateEvent(createEventRequest, userId).toFutureResponse()
    }

    @PostMapping(UserEventRouter.CREATE_USER_EVENT_V1)
    fun createUserEvent(
        @RequestHeader("user_id") userId: Long,
        @Valid @RequestBody request: CreateUserEventRequest,
        @RequestHeader(value = "apikey") apiKey: String
    ): Future<CreateUserEventResponse> {

        return eventService.createUserEvent(userId, request.update(), apiKey).toFutureResponse()
    }
}
