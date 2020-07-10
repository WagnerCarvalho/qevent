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
    @field:JsonProperty("product")
    var product: String = "",

    @field:JsonProperty("description")
    var description: String = "",

    @field:NotNull
    @field:JsonProperty("price")
    var price: Double = "0.0".toDouble(),

    @field:JsonProperty("url")
    var url: String = "",

    @field:JsonProperty("quantity")
    var quantity: Long = 0,

    @field:NotNull
    @field:NotEmpty
    @field:JsonProperty("category")
    var category: String = ""
)
