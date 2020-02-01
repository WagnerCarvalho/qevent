package com.qagile.qmenu.api.utils

import com.fasterxml.jackson.databind.ObjectMapper
import io.reactivex.Observable
import io.reactivex.Single
import java.nio.charset.Charset
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Future
import javax.servlet.http.HttpServletRequest
import okhttp3.Headers
import retrofit2.HttpException

fun <T> Single<T>.toFutureResponse(): Future<T> {
    val future = CompletableFuture<T>()
    this.subscribe({ future.complete(it) }, { ex: Throwable? -> future.completeExceptionally(ex) })

    return future
}

fun <T> Observable<T>.toFutureResponse(): Future<T> {
    val future = CompletableFuture<T>()
    this.subscribe({ future.complete(it) }, { ex: Throwable? -> future.completeExceptionally(ex) })

    return future
}

fun HttpServletRequest.getHeadersInfo(): Map<String, String> {

    val headers = java.util.HashMap<String, String>()
    val headerNames = this.headerNames

    while (headerNames.hasMoreElements()) {
        val key = headerNames.nextElement() as String

        val value = this.getHeader(key)
        headers[key] = value
    }

    return headers
}

fun Map<String, String>.setTextPlain(type: String): Map<String, String> {

    val headers: HashMap<String, String> = this as HashMap<String, String>
    headers["Content-Type"] = type

    return headers
}

fun ArrayList<Map<String, Any>>.convertToMap(): Map<String, String> {

    var response: HashMap<String, String> = hashMapOf()

    this.map {
        response[it["key"] as String] = it["value"].toString()
    }

    return response
}

fun Headers.convertToMap(): Map<String, String> {

    var response: HashMap<String, String> = hashMapOf()

    this.toMultimap().map {

        response[it.key] = it.value.first()
    }

    return response
}

fun Throwable.getError(): String? {
    var errorData = this.toString()

    if (this is HttpException) {
        val source = this.response().errorBody()?.source()
        source?.request(Long.MAX_VALUE)
        val responseBodyString = source?.buffer()?.clone()?.readString(Charset.forName("UTF-8"))
        errorData = responseBodyString.objectToString()
    }

    return errorData
}

fun Any?.objectToString(): String {

    return ObjectMapper().writeValueAsString(this)
}
