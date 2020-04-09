package com.cargill.components.oktacomponentlibrary.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitComponent {

    private fun provideRetrofitComponent(issuerUri: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(issuerUri)
            .client(provideOkHttpClient(RefreshTokenInterceptor()))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun provideOkHttpClient(refreshTokenInterceptor: RefreshTokenInterceptor): OkHttpClient {
        //Log interceptor
        val logInterceptor = HttpLoggingInterceptor()
        logInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient().newBuilder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(refreshTokenInterceptor)
            .addInterceptor(logInterceptor)
            .build()
    }

    fun webService(issuerUri: String): RefreshTokenAPI =
        provideRetrofitComponent(issuerUri).create(RefreshTokenAPI::class.java)



}