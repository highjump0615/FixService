package com.brainyapps.e2fix.activities.serviceman

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.activities.BaseActivity
import com.brainyapps.e2fix.activities.JobDetailHelper
import com.brainyapps.e2fix.models.Bid
import com.brainyapps.e2fix.models.User
import kotlinx.android.synthetic.main.activity_bid_detail.*

class BidDetailActivity : BaseActivity() {

    var helper: JobDetailHelper? = null

    companion object {
        val KEY_BID = "bid"
    }

    var bid: Bid? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bid_detail)

        setNavbar(null, true)

        val bundle = intent.extras

        // get bid from intent
        this.bid = bundle?.getParcelable<Bid>(KEY_BID)

        this.text_bid_time.text = this.bid!!.time
        this.text_bid_price.text = "$${this.bid!!.price}"

        if (this.bid!!.isTaken) {
            this.text_title.text = "BID WON JOB DETILS"

            // hide some views
            this.layout_your_bid.visibility = View.GONE
            this.layout_client_name.visibility = View.GONE
            this.layout_job_location.visibility = View.GONE
            this.layout_client_contact.visibility = View.GONE

            User.readFromDatabase(this.bid!!.job!!.userId, object: User.FetchDatabaseListener {
                override fun onFetchedUser(user: User?, success: Boolean) {

                    // update view
                    this@BidDetailActivity.text_client_name.text = user!!.userFullName()
                    this@BidDetailActivity.text_client_contact.text = user.contact
                }
            })

            this.text_location.text = this.bid!!.job!!.location
        }

        //
        // set stripe background
        //
        var nBgIndex = 0
        for (i in 0 until this.layout_stripe_bg.childCount) {
            val layout = this.layout_stripe_bg.getChildAt(i)

            if (layout.visibility != View.VISIBLE) {
                continue
            }

            if (nBgIndex % 2 == 1) {
                layout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhiteBackground))
            }

            nBgIndex++
        }

        // update job info
        helper = JobDetailHelper(findViewById<View>(android.R.id.content))
        helper!!.fillJobInfo(this.bid!!.job!!)
    }
}
