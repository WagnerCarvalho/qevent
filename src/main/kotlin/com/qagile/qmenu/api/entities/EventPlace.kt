package com.qagile.qmenu.api.entities

import com.fasterxml.jackson.annotation.JsonProperty

data class EventPlace(

    @JsonProperty("address")
    val address: String = "",

    @JsonProperty("neighborhood")
    val neighborhood: String = "",

    @JsonProperty("city")
    val city: String = "",

    @JsonProperty("state")
    val state: String = "",

    @JsonProperty("location")
    val location: EventLocation = EventLocation()
)
