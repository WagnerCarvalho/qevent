package com.qagile.qmenu.api.entities.request

import com.fasterxml.jackson.annotation.JsonProperty
import com.qagile.qmenu.api.domain.EventPlace
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class UpdateEventRequest(

    @field:NotNull
    @field:NotEmpty
    @JsonProperty("id")
    val id: String? = "",

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
)
