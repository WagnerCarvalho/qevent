package com.qagile.qevent.api.domain

import com.fasterxml.jackson.annotation.JsonProperty
import com.qagile.qevent.api.entities.EventLocation
import com.qagile.qevent.api.entities.EventPlace
import com.qagile.qevent.api.entities.EventStatus
import com.qagile.qevent.api.entities.request.CreateEventRequest
import com.qagile.qevent.api.entities.request.UpdateEventRequest
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "event")
data class Event(

    @Id
    @JsonProperty("id")
    var id: String? = ObjectId().toHexString(),

    @JsonProperty("user_id")
    var userId: Long = 0,

    @JsonProperty("name")
    var name: String = "",

    @JsonProperty("description")
    var description: String = "",

    @JsonProperty("email")
    var email: String = "",

    @JsonProperty("image_url")
    var imageUrl: String = "",

    @JsonProperty("version")
    var version: Long = 1,

    @JsonProperty("place")
    var place: EventPlace = EventPlace(),

    @JsonProperty("event_status")
    var eventStatus: EventStatus = EventStatus.PENDING,

    @JsonProperty("created_at")
    var createdAt: LocalDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS),

    @JsonProperty("updated_at")
    var updatedAt: LocalDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)

) {
    fun mergeDataCompany(newEvent: UpdateEventRequest, oldEvent: Event): Event {

        return Event(
            id = oldEvent.id,
            userId = oldEvent.userId,
            name = if (newEvent.name == "") oldEvent.name else newEvent.name,
            description = if (newEvent.description == "") oldEvent.description else newEvent.description,
            email = if (newEvent.email == "") oldEvent.email else newEvent.email,
            imageUrl = if (newEvent.imageUrl == "") oldEvent.imageUrl else newEvent.imageUrl,
            version = oldEvent.version + 1,
            place = EventPlace(
                address = if (newEvent.place.address == "") oldEvent.place.address else newEvent.place.address,
                neighborhood = if (newEvent.place.neighborhood == "") oldEvent.place.neighborhood else newEvent.place.neighborhood,
                city = if (newEvent.place.city == "") oldEvent.place.city else newEvent.place.city,
                state = if (newEvent.place.state == "") oldEvent.place.state else newEvent.place.state,
                location = EventLocation(
                    lat = if (newEvent.place.location.lat == 0.0) oldEvent.place.location.lat else newEvent.place.location.lat,
                    lng = if (newEvent.place.location.lng == 0.0) oldEvent.place.location.lng else newEvent.place.location.lng
                )
            ),
            eventStatus = if (newEvent.eventStatus == "") oldEvent.eventStatus else EventStatus.ACTIVE,
            createdAt = oldEvent.createdAt
        )
    }

    fun convertToEvents(createEventRequest: CreateEventRequest, userId: Long, imageDefault: String): Event {

        return Event(
            userId = userId,
            name = createEventRequest.name,
            description = createEventRequest.description,
            email = createEventRequest.email,
            imageUrl = if (createEventRequest.imageUrl == "") imageDefault else createEventRequest.imageUrl,
            place = EventPlace(
                address = createEventRequest.place.address,
                neighborhood = createEventRequest.place.neighborhood,
                city = createEventRequest.place.city,
                state = createEventRequest.place.state,
                location = EventLocation(
                    lat = createEventRequest.place.location.lat,
                    lng = createEventRequest.place.location.lng
                )
            )

        )
    }

    fun getEventStatus(eventStatus: String): EventStatus {

        return when (eventStatus.toUpperCase()) {
            EventStatus.ACTIVE.name -> EventStatus.ACTIVE
            else -> EventStatus.PENDING
        }
    }
}
