package com.qagile.qmenu.api.controller

import com.qagile.qmenu.api.domain.Event
import com.qagile.qmenu.api.entities.request.CreateEventRequest
import com.qagile.qmenu.api.entities.request.DeleteEventRequest
import com.qagile.qmenu.api.entities.response.DeleteEventResponse
import com.qagile.qmenu.api.routers.EventRouter
import com.qagile.qmenu.api.service.EventService
import com.qagile.qmenu.api.utils.toFutureResponse
import java.util.concurrent.Future
import javax.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
class EventController {

    @Autowired
    private lateinit var eventService: EventService

    @DeleteMapping(EventRouter.DELETE_EVENT_V1)
    fun removeEventV1(
        @Valid @RequestBody deleteEventRequest: DeleteEventRequest,
        @RequestHeader(value = "USER_ID") applicationUserId: Long
    ): Future<DeleteEventResponse> {

        return eventService.checkRemoveEvent(deleteEventRequest, applicationUserId).toFutureResponse()
    }

    @PostMapping(EventRouter.CREATE_EVENT_V1)
    fun createEventV1(
        @Valid @RequestBody createEventRequest: CreateEventRequest,
        @RequestHeader(value = "USER_ID") applicationUserId: Long
    ): Future<Event> {

        return eventService.checkCreateEvent(createEventRequest, applicationUserId).toFutureResponse()
    }
}
