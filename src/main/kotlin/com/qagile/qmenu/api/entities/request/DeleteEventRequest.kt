package com.qagile.qmenu.api.entities.request

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import org.bson.types.ObjectId

data class DeleteEventRequest(

    @field:NotNull
    @field:NotEmpty
    @JsonProperty("id")
    val id: String? = ""
)
