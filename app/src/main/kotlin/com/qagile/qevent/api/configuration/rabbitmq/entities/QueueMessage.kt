package com.qagile.qevent.api.configuration.rabbitmq.entities

import com.fasterxml.jackson.annotation.JsonProperty

class QueueMessage(

    @field:JsonProperty("status_message")
    var statusMessage: String = "",

    @field:JsonProperty("event_id")
    var eventId: String = "",

    @field:JsonProperty("user_id")
    var userId: Long = 0
)
