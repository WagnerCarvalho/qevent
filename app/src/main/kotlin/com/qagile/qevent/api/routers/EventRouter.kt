package com.qagile.qevent.api.routers

object EventRouter {
    const val BASE_PATH = "/v1/company"
    const val CREATE_EVENT_V1 = "$BASE_PATH/"
    const val GET_EVENT_V1 = "$BASE_PATH/{id}"
    const val DELETE_EVENT_V1 = "$BASE_PATH/{id}"
    const val UPDATE_EVENT_V1 = "$BASE_PATH/{id}"
    const val CREATE_USER_EVENT_V1 = "$BASE_PATH/create-user"
}
