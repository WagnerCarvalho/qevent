package com.qagile.qmenu.api.domain

import com.fasterxml.jackson.annotation.JsonProperty

data class CompanyPlace(

    @JsonProperty("address")
    val address: String = "",

    @JsonProperty("neighborhood")
    val neighborhood: String = "",

    @JsonProperty("city")
    val city: String = "",

    @JsonProperty("state")
    val state: String = "",

    @JsonProperty("location")
    val location: CompanyLocation = CompanyLocation()
)
