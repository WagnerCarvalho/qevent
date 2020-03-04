package com.qagile.qevent.api.entities.response

import com.fasterxml.jackson.annotation.JsonProperty

data class DeleteResponse(

    @JsonProperty("id")
    var id: String = String(),

    @JsonProperty("user_id")
    val userId: Long = 0,

    @JsonProperty("msg")
    var message: String = ""
) {
    fun getDeleteEventResponse(id: String, userId: Long, message: String): DeleteResponse {

        return DeleteResponse(
            id = id,
            userId = userId,
            message = message
        )
    }
}
