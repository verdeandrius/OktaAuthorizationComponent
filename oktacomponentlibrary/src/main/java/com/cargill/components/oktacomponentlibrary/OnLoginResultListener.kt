package com.cargill.poultrylatam.features.components.oktaauthorization

import com.okta.oidc.clients.web.WebAuthClient
import com.okta.oidc.util.AuthorizationException

interface OnLoginResultListener {

    fun onLoginResult(client: WebAuthClient)

    fun onErrorResult(msg: String?, error: AuthorizationException?)

}