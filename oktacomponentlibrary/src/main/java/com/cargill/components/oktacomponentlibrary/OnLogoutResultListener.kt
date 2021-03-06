package com.cargill.components.oktacomponentlibrary

import com.okta.oidc.clients.web.WebAuthClient
import com.okta.oidc.util.AuthorizationException

interface OnLogoutResultListener {

    fun onLogoutResult(client: WebAuthClient)

}