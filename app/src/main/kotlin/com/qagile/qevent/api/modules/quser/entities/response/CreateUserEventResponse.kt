package com.qagile.qevent.api.modules.quser.entities.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class CreateUserEventResponse(

    @JsonProperty("event_id")
    val eventId: String = "",

    @JsonProperty("user_id")
    val userId: Long = 0

)
