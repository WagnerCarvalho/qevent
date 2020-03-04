package com.qagile.qevent.api.entities.response

import com.fasterxml.jackson.annotation.JsonProperty

data class DeleteResponse(

    @JsonProperty("id")
    var id: String = String(),

    @JsonProperty("application_user_id")
    val applicationUserId: Long = 0,

    @JsonProperty("msg")
    var message: String = ""
) {
    fun getDeleteEventResponse(id: String, applicationUserId: Long, message: String): DeleteResponse {

        return DeleteResponse(
            id = id,
            applicationUserId = applicationUserId,
            message = message
        )
    }
}
