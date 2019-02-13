package com.brainyapps.ezfix.activities.customer

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.brainyapps.ezfix.R
import com.brainyapps.ezfix.activities.StripeCardInputActivity
import com.brainyapps.ezfix.activities.UserDetailHelper
import com.brainyapps.ezfix.activities.signin.SignupStripeActivity
import com.brainyapps.ezfix.api.APIManager
import com.brainyapps.ezfix.models.Bid
import com.brainyapps.ezfix.models.Job
import com.brainyapps.ezfix.models.User
import com.brainyapps.ezfix.utils.CommonObjects
import com.brainyapps.ezfix.utils.Utils
import kotlinx.android.synthetic.main.activity_customer_payment_confirm.*
import kotlinx.android.synthetic.main.layout_payment_item.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.net.URLDecoder

class BidPayActivity : SignupStripeActivity(), View.OnClickListener {

    private val TAG = BidPayActivity::class.java.getSimpleName()

    var helper: UserDetailHelper? = null

    var bid: Bid? = null
    var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setContentView(R.layout.activity_customer_payment_confirm)

        setNavbar(null, true)

        // get bid from common objects
        bid = CommonObjects.selectedBid!!
        job = CommonObjects.selectedJob!!

        // payment info
        updateCardItem()
        enableProceedButton()

        but_add_payment.setOnClickListener(this)
        layout_card_item.setOnClickListener(this)

        // user info
        helper = UserDetailHelper(findViewById<View>(android.R.id.content))
        helper!!.fillUserInfo(bid!!.user!!)

        // bind info
        text_bid_time.text = bid!!.time
        text_bid_price.text = "$${bid!!.price}"

        // proceed
        this.but_proceed.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        super.onClick(view)

        when (view?.id) {
            // proceed
            R.id.but_proceed -> {
                doPayment()
            }
        }
    }

    private fun enableProceedButton() {
        enableButton(this.but_proceed, stripeSrc != null)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CODE_CARD_INFO) {
            enableProceedButton()
        }
    }

    private fun doPayment() {

        val user = User.currentUser!!

        if (TextUtils.isEmpty(user.stripeCustomerId)) {
            Utils.createErrorAlertDialog(this,
                    "Payment cannot be done",
                    "payment information is not initialized"
            ).show()

            return
        }
        if (TextUtils.isEmpty(bid!!.user!!.stripeAccountId)) {
            Utils.createErrorAlertDialog(this,
                    "Payment cannot be done",
                    "The serviceman is not ready to get payment"
            ).show()

            return
        }

        Utils.createProgressDialog(this, "Proceeding", "Payment is in progress...")

        APIManager.createStripeCharge(
                user.stripeCustomerId,
                (bid!!.price * 100).toInt(),
                "Payment for ${job!!.title}",
                bid!!.user!!.stripeAccountId,
                getString(R.string.stripe_secret_key),
                object: Callback {
                    override fun onFailure(call: Call?, e: IOException?) {
                        Log.w(TAG, "create stripe charge failed", e)

                        Toast.makeText(this@BidPayActivity, "Payment failed", Toast.LENGTH_SHORT).show()
                        Utils.closeProgressDialog()
                    }

                    override fun onResponse(call: Call?, response: Response?) {
                        val strResponseDecoded = URLDecoder.decode(response!!.body()!!.string(), "UTF-8")
                        val result = JSONObject(strResponseDecoded)

                        if (!response.isSuccessful) {
                            runOnUiThread {
                                Toast.makeText(this@BidPayActivity, "Payment failed", Toast.LENGTH_SHORT).show()
                                Utils.closeProgressDialog()
                            }

                            Log.d(TAG, strResponseDecoded)
                            return
                        }

                        // save bid info
                        bid!!.isTaken = true
                        bid!!.saveToDatabase(bid!!.id)

                        // save job
                        job!!.bidTakenId = bid!!.id
                        job!!.saveToDatabase(job!!.id, Job.FIELD_BIDID_TAKEN, bid!!.id)

                        runOnUiThread {
                            Toast.makeText(this@BidPayActivity, "Payment succeeded", Toast.LENGTH_SHORT).show()

                            finish()
                        }
                    }
                }
        )
    }
}
