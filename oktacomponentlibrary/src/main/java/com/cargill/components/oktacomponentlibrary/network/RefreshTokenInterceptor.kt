package com.cargill.components.oktacomponentlibrary.network

import okhttp3.Interceptor
import okhttp3.Response

class RefreshTokenInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        //Added token to each request
        val request = chain.request().newBuilder()
            .addHeader("Content-Type", "application/x-www-form-urlencoded")
            .addHeader("Accept", "application/json")
            .build()

        return chain.proceed(request)
    }
}