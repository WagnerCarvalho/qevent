package com.qagile.qevent.api.modules.quser.entities.request

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.qagile.qevent.api.entities.DefaultRequest
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
    val eventId: String = "",

    @field:JsonProperty("grant_type")
    val grantType: String = ""

) {
    fun update(): CreateUserEventRequest {

        return CreateUserEventRequest(
            authorizationId = this.authorizationId,
            acquirer = this.acquirer,
            eventId = this.eventId,
            grantType = if (this.grantType == "") DefaultRequest.authorizationCode else this.grantType
        )
    }
}
