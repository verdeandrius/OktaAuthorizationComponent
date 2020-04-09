package com.cargill.components.oktacomponentlibrary.network

import androidx.annotation.Nullable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface RefreshTokenAPI {

    @FormUrlEncoded
    @POST("v1/token/")
    suspend fun callRefreshToken(
        @Field("client_id") clientId: String,
        @Field("refresh_token") refreshToken: String,
        @Field("grant_type") @Nullable grant_type: String = "refresh_token",
        @Field("scope") @Nullable scope: String = "openid profile email offline_access"
    ): Response<ResponseBody>

}