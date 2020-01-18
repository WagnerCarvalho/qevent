package com.qagile.qmenu.api.entities.request

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class DeleteRequest(
    @field:NotNull
    @field:NotEmpty
    @JsonProperty("id")
    val id: String = ""
)
