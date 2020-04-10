package com.cargill.components.oktacomponentlibrary

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.Log
import androidx.annotation.ColorInt
import androidx.annotation.Nullable
import com.cargill.components.oktacomponentlibrary.network.RetrofitComponent
import com.cargill.poultrylatam.features.components.oktaauthorization.OnLogoutResultListener
import com.okta.oidc.*
import com.okta.oidc.clients.web.WebAuthClient
import com.okta.oidc.storage.SharedPreferenceStorage
import com.okta.oidc.util.AuthorizationException
import org.json.JSONObject
import java.util.concurrent.Executors

object OktaAuthorization {

    lateinit var config: OIDCConfig
    lateinit var onLoginResult: OnLoginResultListener
    lateinit var onLogoutResultListener: OnLogoutResultListener
    private lateinit var client: WebAuthClient
    private lateinit var tokenEndpoint: String
    private lateinit var context: Context

    fun initialize(@Nullable @ColorInt webViewColor: Int = Color.DKGRAY) {
        client = Okta.WebAuthBuilder()
            .withConfig(config)
            .withContext(context.applicationContext)
            .withStorage(SharedPreferenceStorage(context.applicationContext))
            .withCallbackExecutor(Executors.newSingleThreadExecutor())
            .withTabColor(webViewColor)
            .supportedBrowsers("com.android.chrome", "org.mozilla.firefox")
            .create()
    }

    fun setOktaConfigParams(
        clientId: String,
        redirectUri: String,
        endSessionRedirectUri: String,
        discoveryUri: String,
        context: Context
    ) {
        config = OIDCConfig.Builder()
            .clientId(clientId)
            .redirectUri(redirectUri)
            .endSessionRedirectUri(endSessionRedirectUri)
            .discoveryUri(discoveryUri)
            .scopes(
                "openid",
                "profile",
                "email",
                "offline_access"
            )
            .create()
        OktaAuthorization.context = context
    }

    fun doLogin(activity: Activity) {
        registerCallbackForAuthActions(
            activity
        )
        client.signIn(activity, null)
    }

    fun doLogout(activity: Activity) {
        registerCallbackForAuthActions(
            activity
        )
        Log.d("doLogout", "PRESSED <<<")
        client.signOut(activity, object : RequestCallback<Int, AuthorizationException> {
            override fun onSuccess(result: Int) {
                onLogoutResultListener.onLogoutResult(
                    client
                )
            }

            override fun onError(msg: String?, error: AuthorizationException?) {
                Log.d(
                    "onError", error!!.error +
                            " signOut onError " + msg, error
                )
                println(">>> OKTA ERROR:\n" + msg!!)
            }

        })
    }

    /**
     * @return Success response return new access_token
     * @return failure response return an empty string
     */

    suspend fun doRefreshToken(
        refreshToken: String,
        issuerUri: String,
        clientId: String
    ): String? {
        val serviceResponse = RetrofitComponent().webService(issuerUri)
            .callRefreshToken(refreshToken, clientId)
        return if (serviceResponse.isSuccessful) {

            @Suppress("BlockingMethodInNonBlockingContext")
            val refreshTokenJsonResponse= JSONObject(serviceResponse.body()?.string()!!)

            refreshTokenJsonResponse.getString("access_token")
        } else {
            null
        }
    }

    private fun registerCallbackForAuthActions(activity: Activity) {
        client.registerCallback(object :
            ResultCallback<AuthorizationStatus, AuthorizationException> {
            override fun onSuccess(result: AuthorizationStatus) {
                when (result) {
                    AuthorizationStatus.AUTHORIZED -> {
                        try {
                            Log.d("AuthorizationStatus -> onSuccess", "Signed out!")
                            onLoginResult.onLoginResult(
                                client
                            )

                        } catch (e: AuthorizationException) {
                            Log.d("AuthorizationStatus -> onSuccess", "Login failed")
                            e.printStackTrace()
                        }
                    }
                    AuthorizationStatus.SIGNED_OUT -> {
                        Log.d("AuthorizationStatus -> onSuccess", "Signed out!")
                        onLogoutResultListener.onLogoutResult(
                            client
                        )
                    }

                    else -> {
                        onLoginResult.onErrorResult("CANCELED, ERROR, EMAIL_VERIFICATION_AUTHENTICATED, EMAIL_VERIFICATION_UNAUTHENTICATED", null)
                    }
                }
            }

            override fun onCancel() {
                Log.d("onCancel", "Web View was closed before complete the login process. CANCELED!")
                onLoginResult.onWebViewCanceled()
            }

            override fun onError(msg: String?, error: AuthorizationException?) {
                Log.d(
                    "onError", error!!.error +
                            msg, error
                )
                println(">>> OKTA ERROR:\n" + msg!!)
                onLoginResult.onErrorResult(msg, error)
            }

        }, activity)
    }

}