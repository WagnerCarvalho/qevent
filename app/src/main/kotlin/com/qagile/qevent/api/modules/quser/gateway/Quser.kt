package com.qagile.qevent.api.modules.quser.gateway

import com.qagile.qevent.api.modules.quser.entities.request.CreateUserEventRequest
import com.qagile.qevent.api.modules.quser.entities.response.CreateUserEventResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.POST

interface Quser {

    @POST("/quser/v1/user/create-event")
    fun createUserEvent(
        @HeaderMap headers: Map<String, String>,
        @Body request: CreateUserEventRequest
    ): Single<CreateUserEventResponse>
}
