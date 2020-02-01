package com.qagile.qmenu.api.entities.request

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class UpdateMenuRequest(

    @field:NotNull
    @field:NotEmpty
    @JsonProperty("id")
    val id: String = "",

    @JsonProperty("product")
    val product: String = "",

    @JsonProperty("description")
    val description: String = "",

    @JsonProperty("status")
    val status: String = "",

    @JsonProperty("price")
    val price: Double = "0.0".toDouble()

)
