package com.brainyapps.ezfix.activities.signin

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.brainyapps.ezfix.R
import com.brainyapps.ezfix.activities.BasePaymentInfoActivity
import com.brainyapps.ezfix.activities.StripeCardInputActivity
import com.brainyapps.ezfix.api.APIManager
import com.brainyapps.ezfix.models.StripeSource
import com.brainyapps.ezfix.models.User
import com.brainyapps.ezfix.utils.Utils
import kotlinx.android.synthetic.main.activity_signup_stripe.*
import kotlinx.android.synthetic.main.layout_payment_item.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.net.URLDecoder

open class SignupStripeActivity : BasePaymentInfoActivity(), View.OnClickListener {

    private val TAG = SignupStripeActivity::class.java.getSimpleName()
    val CODE_CARD_INFO = 2000

    var stripeSrc: StripeSource? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        createStripeCustomer()
    }

    open fun initView() {
        setContentView(R.layout.activity_signup_stripe)

        setNavbar("Payment Information", true)

        this.but_done.setOnClickListener(this)

        this.but_add_payment.setOnClickListener(this)
        this.layout_card_item.setOnClickListener(this)

        updateCardItem()
    }

    fun updateCardItem() {
        // update payment info
        stripeSrc = User.currentUser!!.stripeSource
        this.carditem.fillContent(stripeSrc)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CODE_CARD_INFO) {
            // get stripe source from intent
            val bundle = data?.extras
            bundle?.let {
                // update payment info
                stripeSrc = it.getParcelable<StripeSource>(StripeCardInputActivity.KEY_STRIPE_SOURCE)
                this.carditem.fillContent(stripeSrc!!)
            }
        }
    }

    /**
     * check stripe customer, create it if not exists
     */
    private fun createStripeCustomer() {
        val user = User.currentUser!!

        if (!TextUtils.isEmpty(user.stripeCustomerId)) {
            return
        }

        APIManager.createStripeCustomer(
                user.email,
                getString(R.string.stripe_secret_key),
                object: Callback {
                    override fun onFailure(call: Call?, e: IOException?) {
                        Log.w(TAG, "create stripe customer failed", e)

                        Toast.makeText(this@SignupStripeActivity, "Failed creating stripe customer", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call?, response: Response?) {
                        val strResponseDecoded = URLDecoder.decode(response!!.body()!!.string(), "UTF-8")
                        val result = JSONObject(strResponseDecoded)

                        if (!response.isSuccessful) {
                            runOnUiThread {
                                Toast.makeText(this@SignupStripeActivity, "Failed creating stripe customer", Toast.LENGTH_SHORT).show()
                            }

                            Log.d(TAG, strResponseDecoded)
                            return
                        }

                        runOnUiThread {
                            Toast.makeText(this@SignupStripeActivity, "Created stripe customer successfully", Toast.LENGTH_SHORT).show()
                        }

                        // save bid info
                        user.stripeCustomerId = result.getString("id")
                    }
                }
        )
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.but_done -> {
                saveUserInfo()
            }

            // payment method
            R.id.but_add_payment, R.id.layout_card_item -> {
                goToCardInput()
            }
        }
    }

    private fun goToCardInput() {
        val intent = Intent(this, StripeCardInputActivity::class.java)
        startActivityForResult(intent, CODE_CARD_INFO)
    }

    fun updateSourceInUserDb(userId: String) {
        val user = User.currentUser!!

        // save user info
        user.stripeSource = stripeSrc
        user.saveToDatabase(userId)
    }

    override fun saveUserData(userId: String) {
        updateSourceInUserDb(userId)

        //
        // STRIPE: attach source to customer
        //
        val user = User.currentUser!!
        if (!TextUtils.isEmpty(user.stripeCustomerId) && stripeSrc != null) {
            Utils.createProgressDialog(this, "Stripe", "Attaching card info to customer")

            // STRIPE: attach source to customer
            APIManager.attachSource(
                    stripeSrc!!.sourceId,
                    user.stripeCustomerId,
                    getString(R.string.stripe_secret_key),
                    object : Callback {
                        override fun onFailure(call: Call?, e: IOException?) {
                            Log.w(TAG, "attach source failed", e)

                            Toast.makeText(this@SignupStripeActivity, "Failed attaching source", Toast.LENGTH_SHORT).show()
                            Utils.closeProgressDialog()
                        }

                        override fun onResponse(call: Call?, response: Response?) {
                            val strResponseDecoded = URLDecoder.decode(response!!.body()!!.string(), "UTF-8")
                            val result = JSONObject(strResponseDecoded)

                            if (!response.isSuccessful) {
                                runOnUiThread {
                                    Toast.makeText(this@SignupStripeActivity, "Failed attaching source", Toast.LENGTH_SHORT).show()
                                }

                                Log.d(TAG, strResponseDecoded)
                                Utils.closeProgressDialog()
                                return
                            }

                            runOnUiThread {
                                Toast.makeText(this@SignupStripeActivity, "Attached card information successfully", Toast.LENGTH_SHORT).show()
                                finalize()
                            }
                        }
                    }
            )
        }
        else {
            finalize()
        }
    }

    private fun finalize() {
        if (fromSignup) {
            goToMain()
            return
        }

        finish()
    }
}
