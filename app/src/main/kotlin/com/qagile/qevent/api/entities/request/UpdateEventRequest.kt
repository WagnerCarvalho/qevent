package com.qagile.qevent.api.entities.request

import com.fasterxml.jackson.annotation.JsonProperty
import com.qagile.qevent.api.entities.EventPlace

data class UpdateEventRequest(

    @JsonProperty("id")
    var id: String = "",

    @JsonProperty("name")
    var name: String = "",

    @JsonProperty("description")
    var description: String = "",

    @JsonProperty("email")
    var email: String = "",

    @JsonProperty("image_url")
    var imageUrl: String = "",

    @JsonProperty("place")
    var place: EventPlace = EventPlace()
) {
    fun get(id: String): UpdateEventRequest {

        this.id = id
        return this
    }
}
