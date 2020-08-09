package com.qagile.qevent.api.modules.qacquirer.gateway

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.HeaderMap

interface Qacquirer {
    @GET("/qacquirer/v1/search-user")
    fun getUserAcquirer(
        @HeaderMap headers: Map<String, String>
    ): Single<Map<String, Any>>
}
