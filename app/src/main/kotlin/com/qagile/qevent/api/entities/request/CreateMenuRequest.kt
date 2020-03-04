package com.qagile.qevent.api.entities.request

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class CreateMenuRequest(

    @field:NotNull
    @field:NotEmpty
    @JsonProperty("event_id")
    var eventId: String = "",

    @field:NotNull
    @field:NotEmpty
    @JsonProperty("product")
    val product: String = "",

    @JsonProperty("description")
    val description: String = "",

    @field:NotNull
    @JsonProperty("price")
    val price: Double = "0.0".toDouble(),

    @JsonProperty("url")
    val url: String = ""

)
