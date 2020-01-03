package com.qagile.qmenu.api.utils

import io.reactivex.Observable
import io.reactivex.Single
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Future
import javax.servlet.http.HttpServletRequest
import okhttp3.Headers

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

