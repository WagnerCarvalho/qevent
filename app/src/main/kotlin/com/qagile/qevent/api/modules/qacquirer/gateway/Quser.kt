package com.qagile.qevent.api.modules.qacquirer.gateway

import com.qagile.qevent.api.modules.qacquirer.entities.request.CreateUserEventRequest
import com.qagile.qevent.api.modules.qacquirer.entities.response.CreateUserEventResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.POST

interface Quser {

    @POST("/v1/user/create-event")
    fun createUserEvent(
        @HeaderMap headers: Map<String, String>,
        @Body request: CreateUserEventRequest
    ): Single<CreateUserEventResponse>
}
