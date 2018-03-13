package com.brainyapps.e2fix.api

import android.net.Uri
import android.text.TextUtils
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.collections.HashMap

/**
 * Created by Administrator on 3/14/18.
 */
object APIManager {

    const val StripBaseUrl = "https://api.stripe.com/v1"

    /**
     * API for getting strip account id
     */
    fun getStripeAccountId(authCode: String,
                           secret: String,
                           responseCallback: Callback) {

        val urlStripe = "https://connect.stripe.com/oauth/token"

        val params: MutableMap<String, String> = HashMap()
        params["client_secret"] = secret
        params["code"] = authCode
        params["grant_type"] = "authorization_code"

        sendToServiceByPost(urlStripe, params, responseCallback)
    }

    /**
     * API for getting strip account detail
     */
    fun getStripeAccountDetail(accId: String,
                               secret: String,
                               responseCallback: Callback) {

        val urlStripe = "$StripBaseUrl/accounts/$accId"
        val params: MutableMap<String, String> = HashMap()

        sendToServiceByGet(urlStripe, secret, params, responseCallback)
    }


    /**
     * Send GET request
     */
    private fun sendToServiceByGet(serviceAPIURL: String,
                                   secret: String,
                                   params: Map<String, Any>,
                                   responseCallback: Callback) {

        val uriBuilder = Uri.parse(serviceAPIURL).buildUpon()
        for (param in params) {
            uriBuilder.appendQueryParameter(param.key, param.value.toString())
        }
        val strUrl = uriBuilder.build().toString()

        val requestBuilder = Request.Builder()
        if (!TextUtils.isEmpty(secret)) {
            requestBuilder.addHeader("Authorization", "Bearer $secret")
        }

        val request = requestBuilder.url(strUrl).get().build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(responseCallback)
    }

    /**
     * Send Post request
     */
    private fun sendToServiceByPost(serviceAPIURL: String,
                                    params: Map<String, Any>,
                                    responseCallback: Callback) {

        val formParam = FormBody.Builder()
        for (param in params) {
            formParam.add(param.key, param.value.toString())
        }

        val request = Request.Builder()
                .url(serviceAPIURL)
                .post(formParam.build())
                .build()


        val client = OkHttpClient()
        client.newCall(request).enqueue(responseCallback)
    }
}