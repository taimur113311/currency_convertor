package com.currencyconvertor.data.remote.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class AccessKeyInterceptor(private val accessKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestWithAccessKey = originalRequest.newBuilder()
            .addHeader("access_key", accessKey)
            .build()
        return chain.proceed(requestWithAccessKey)
    }
}