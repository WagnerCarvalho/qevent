package com.qagile.qevent.api.routers

object EventRouter {
    const val BASE_PATH = "/v1/event"
    const val CREATE_EVENT_V1 = "$BASE_PATH/create"
    const val DELETE_EVENT_V1 = "$BASE_PATH/delete/{id}"
    const val UPDATE_EVENT_V1 = "$BASE_PATH/update/{id}"
    const val CREATE_USER_EVENT_V1 = "$BASE_PATH/create-user"
}
