package com.qagile.qevent.api.configuration.rabbitmq.entities

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties
data class UserAcquirer(

    @field:JsonProperty("event_id")
    var eventId: String = "",

    @field:JsonProperty("user_id")
    var userId: Long = 0

)
