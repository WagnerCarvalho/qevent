package com.qagile.qevent.api.modules.qacquirer.entities.request

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class CreateUserEventRequest(

    @JsonProperty("token")
    val token: String = "",

    @JsonProperty("acquirer")
    val acquirer: String = "",

    @JsonProperty("event_id")
    val eventId: String = ""

)
