package com.qagile.qevent.api.manager.entities

data class ErrorData(
    var code: String = "",
    var message: String = "",
    var status: String? = "400",
    val orderIds: List<Int>? = null,
    val statusCode: String? = null
)
