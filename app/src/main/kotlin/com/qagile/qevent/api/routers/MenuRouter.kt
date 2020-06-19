package com.qagile.qevent.api.routers

object MenuRouter {
    const val BASE_PATH = "/v1/menu"
    const val CREATE_MENU_V1 = "$BASE_PATH/"
    const val DELETE_MENU_V1 = "$BASE_PATH/{id}"
    const val UPDATE_MENU_V1 = "$BASE_PATH/{id}"
    const val GET_MENU_V1 = "$BASE_PATH/{id}"
    const val GET_MENU_ALL_V1 = "$BASE_PATH/all/{id}"
}
