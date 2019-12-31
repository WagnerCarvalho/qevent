package com.qagile.qmenu.api.entities.response

import com.fasterxml.jackson.annotation.JsonProperty

data class DeleteEventResponse(

    @JsonProperty("id")
    var id: String? = String(),

    @JsonProperty("application_user_id")
    var applicationUserId: String? = "",

    @JsonProperty("msg")
    var message: String = ""
) {
    fun getDeleteEventResponse(id: String, applicationUserId: Long): DeleteEventResponse {

        return DeleteEventResponse(
            id = id,
            applicationUserId = applicationUserId.toString(),
            message = "Event Successfully Removed"
        )
    }
}
