package com.qagile.qevent.api.modules.qacquirer.entities.request

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty

@JsonIgnoreProperties(ignoreUnknown = true)
data class CreateUserEventRequest(

    @field:NotEmpty
    @field:NotBlank
    @field:JsonProperty("authorization_id")
    val authorizationId: String = "",

    @field:NotEmpty
    @field:NotBlank
    @field:JsonProperty("acquirer")
    val acquirer: String = "",

    @field:NotEmpty
    @field:NotBlank
    @field:JsonProperty("event_id")
    val eventId: String = ""

)
