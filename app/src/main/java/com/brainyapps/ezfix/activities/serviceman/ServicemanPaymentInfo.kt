package com.brainyapps.ezfix.activities.serviceman

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.brainyapps.ezfix.R
import com.brainyapps.ezfix.activities.BaseActivity
import com.brainyapps.ezfix.activities.BasePaymentInfoActivity
import com.brainyapps.ezfix.api.APIManager
import com.brainyapps.ezfix.models.User
import com.brainyapps.ezfix.utils.RandomString
import kotlinx.android.synthetic.main.activity_serviceman_payment_info.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.net.URLDecoder
import java.util.*

class ServicemanPaymentInfo : BasePaymentInfoActivity(), View.OnClickListener {

    private val TAG = ServicemanPaymentInfo::class.java.getSimpleName()

    private var accountId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_serviceman_payment_info)

        setNavbar("Payment Information", true)

        // init button
        enableButton(this.but_done, false)
        this.but_done.setOnClickListener(this)

        // init webview
        val strState = RandomString(6, Random(), RandomString.alphanum).nextString()
        var strStripeAuthUrl = "https://connect.stripe.com/express/oauth/authorize?"
        strStripeAuthUrl += "client_id=${getString(R.string.stripe_client_id)}&"
        strStripeAuthUrl += "state=${strState}"

        this.webview.settings.javaScriptEnabled = true
        this.webview.settings.setSupportZoom(true)
        this.webview.settings.builtInZoomControls = true
        this.webview.loadUrl(strStripeAuthUrl)

        this.webview.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                Log.w(TAG, url)

                if (!url.startsWith(getString(R.string.stripe_redirect_url))) {
                    return
                }

                // hide the web view
                this@ServicemanPaymentInfo.webview.visibility = View.GONE

                // parse params
                val uriInfo = Uri.parse(url)
                val strStateReturned = uriInfo.getQueryParameter("state")
                val strCode = uriInfo.getQueryParameter("code")

                if (!TextUtils.equals(strState, strStateReturned)) {
                    Toast.makeText(this@ServicemanPaymentInfo, "State Code Mismatch", Toast.LENGTH_LONG).show()
                    closeProgress()
                    return
                }

                getAccountId(strCode)
            }
        }
    }

    fun getAccountId(authCode: String) {
        APIManager.getStripeAccountId(
                authCode,
                getString(R.string.stripe_secret_key),
                object: Callback {
                    override fun onFailure(call: Call?, e: IOException?) {
                        runOnUiThread {
                            Toast.makeText(this@ServicemanPaymentInfo, "Failed to get account Id", Toast.LENGTH_SHORT).show()
                            closeProgress()
                        }
                    }

                    override fun onResponse(call: Call?, response: Response?) {
                        val strResponseDecoded = URLDecoder.decode(response!!.body()!!.string(), "UTF-8")
                        val result = JSONObject(strResponseDecoded)
                        getAccountDetail(result.getString("stripe_user_id"))
                    }
                }
        )
    }

    fun getAccountDetail(accId: String) {
        accountId = accId

        APIManager.getStripeAccountDetail(
                accId,
                getString(R.string.stripe_secret_key),
                object: Callback {
                    override fun onFailure(call: Call?, e: IOException?) {
                        Toast.makeText(this@ServicemanPaymentInfo, "Failed to get account info", Toast.LENGTH_SHORT).show()
                        closeProgress()
                    }

                    override fun onResponse(call: Call?, response: Response?) {
                        val strResponseDecoded = URLDecoder.decode(response!!.body()!!.string(), "UTF-8")
                        val result = JSONObject(strResponseDecoded)

                        runOnUiThread {
                            Toast.makeText(this@ServicemanPaymentInfo, "Fetched account successfully", Toast.LENGTH_SHORT).show()
                            enableButton(this@ServicemanPaymentInfo.but_done, true)
                            closeProgress()

                            // update info
                            this@ServicemanPaymentInfo.text_account_email.text = result.getString("email")
                            this@ServicemanPaymentInfo.text_account_country.text = result.getString("country")
                        }
                    }
                }
        )
    }

    fun closeProgress() {
        this.layout_progress.visibility = View.GONE
        this.layout_account.visibility = View.VISIBLE
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            // Photo
            R.id.but_done -> {
                saveUserInfo()
            }
        }
    }

    override fun saveUserData(userId: String) {
        val user = User.currentUser!!

        // save account id
        user.stripeAccountId = accountId
        user.saveToDatabase(userId)

        if (fromSignup) {
            goToMain()
            return
        }

        finish()
    }
}
