package com.qagile.qevent.api.entities

import com.fasterxml.jackson.annotation.JsonProperty

data class EventLocation(
    @JsonProperty("lat")
    val lat: Double = 0.0,

    @JsonProperty("lng")
    val lng: Double = 0.0
)
