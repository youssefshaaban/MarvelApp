package com.example.data.network

import com.example.data.BuildConfig
import com.example.data.utils.md5
import okhttp3.Interceptor
import okhttp3.Response
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QueryAuthenticatorInterceptor @Inject constructor() :Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url

        // Add the query parameters
        val time=Date().time
        val hash= md5("$time${BuildConfig.PRIVATE_KEY}${BuildConfig.API_KEY}")
        val urlWithParams = originalUrl.newBuilder()
            .addQueryParameter("ts", "$time")
            .addQueryParameter("apikey", BuildConfig.API_KEY)
            .addQueryParameter("hash", hash)
            .build()
        val requestWithParams = originalRequest.newBuilder()
            .url(urlWithParams)
            .build()
        return chain.proceed(requestWithParams)
    }
}