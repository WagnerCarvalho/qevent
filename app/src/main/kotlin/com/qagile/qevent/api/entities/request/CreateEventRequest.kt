package com.qagile.qevent.api.entities.request

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.qagile.qevent.api.entities.EventPlace
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@JsonIgnoreProperties(ignoreUnknown = true)
data class CreateEventRequest(

    @field:NotNull
    @field:NotEmpty
    @JsonProperty("name")
    var name: String = "",

    @JsonProperty("description")
    var description: String = "",

    @field:NotNull
    @field:NotEmpty
    @JsonProperty("email")
    var email: String = "",

    @JsonProperty("image_url")
    var imageUrl: String = "",

    @JsonProperty("place")
    var place: EventPlace = EventPlace()
)
