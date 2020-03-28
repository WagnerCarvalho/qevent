package com.qagile.qevent.api.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.qagile.qevent.api.modules.qacquirer.gateway.Quser
import okhttp3.OkHttpClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit

@Configuration
class QuserConfiguration(
    @Value("\${url_quser}") val url: String,
    @Value("\${time_out_quser}") val timeOut: Long
) {

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Bean
    fun okHttpClient(): OkHttpClient =

        OkHttpClient.Builder()
            .connectTimeout(timeOut, TimeUnit.MILLISECONDS)
            .readTimeout(timeOut, TimeUnit.MILLISECONDS)
            .writeTimeout(timeOut, TimeUnit.MILLISECONDS)
            .hostnameVerifier { _, _ -> true }
            .build()

    @Bean
    fun retrofit(okHttpClient: OkHttpClient): Retrofit {

        return Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Bean
    fun gatewayMercadoPago(retrofit: Retrofit): Quser = retrofit.create(Quser::class.java)

}