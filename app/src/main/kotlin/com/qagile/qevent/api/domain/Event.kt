package com.qagile.qevent.api.domain

import com.fasterxml.jackson.annotation.JsonProperty
import com.qagile.qevent.api.entities.EventLocation
import com.qagile.qevent.api.entities.EventPlace
import com.qagile.qevent.api.entities.request.CreateEventRequest
import com.qagile.qevent.api.entities.request.UpdateEventRequest
import com.qagile.qevent.api.entities.EventStatus
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "event")
data class Event(

    @Id
    @JsonProperty("id")
    val id: String? = ObjectId().toHexString(),

    @JsonProperty("user_id")
    val userId: Long = 0,

    @JsonProperty("name")
    val name: String = "",

    @JsonProperty("description")
    val description: String = "",

    @JsonProperty("email")
    val email: String = "",

    @JsonProperty("image_url")
    val imageUrl: String = "",

    @JsonProperty("version")
    val version: Long = 1,

    @JsonProperty("place")
    val place: EventPlace = EventPlace(),

    @JsonProperty("event_status")
    val eventStatus: EventStatus = EventStatus.PENDING

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
            eventStatus = oldEvent.eventStatus
        )
    }

    fun convertToEvents(createEventRequest: CreateEventRequest, userId: Long): Event {

        return Event(
            userId = userId,
            name = createEventRequest.name,
            description = createEventRequest.description,
            email = createEventRequest.email,
            imageUrl = createEventRequest.imageUrl,
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
}
