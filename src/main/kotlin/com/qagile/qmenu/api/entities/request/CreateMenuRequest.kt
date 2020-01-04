package com.qagile.qmenu.api.entities.request

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal
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
    @field:NotEmpty
    @JsonProperty("price")
    val price: BigDecimal = "0.00".toBigDecimal()
)
