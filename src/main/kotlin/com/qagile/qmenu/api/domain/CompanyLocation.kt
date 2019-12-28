package com.qagile.qmenu.api.domain

import com.fasterxml.jackson.annotation.JsonProperty

data class CompanyLocation(
    @JsonProperty("lat")
    val lat: Double = 0.0,

    @JsonProperty("lng")
    val lng: Double = 0.0
)
