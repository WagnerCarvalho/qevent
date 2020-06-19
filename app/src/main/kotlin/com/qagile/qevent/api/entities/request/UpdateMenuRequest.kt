package com.qagile.qevent.api.entities.request

import com.fasterxml.jackson.annotation.JsonProperty

data class UpdateMenuRequest(

    @JsonProperty("id")
    var id: String = "",

    @JsonProperty("product")
    val product: String = "",

    @JsonProperty("description")
    val description: String = "",

    @JsonProperty("status")
    val status: String = "",

    @JsonProperty("price")
    val price: Double = "0.0".toDouble(),

    @JsonProperty("url")
    val url: String = "",

    @JsonProperty("quantity")
    val quantity: Long = 0

) {
    fun get(id: String): UpdateMenuRequest {

        this.id = id
        return this
    }
}
