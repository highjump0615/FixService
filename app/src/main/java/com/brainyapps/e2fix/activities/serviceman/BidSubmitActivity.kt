package com.brainyapps.e2fix.activities.serviceman

import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
import android.view.TextureView
import android.view.View
import android.widget.TextView
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.activities.BaseActivity
import com.brainyapps.e2fix.activities.JobDetailHelper
import com.brainyapps.e2fix.models.Bid
import com.brainyapps.e2fix.models.Job
import com.brainyapps.e2fix.models.User
import com.brainyapps.e2fix.utils.Utils
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_bid_submit.*

class BidSubmitActivity : BaseActivity(), View.OnClickListener {

    var helper: JobDetailHelper? = null
    var job: Job? = null
    var bid: Bid? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bid_submit)

        setNavbar(null, true)

        this.but_submit.setOnClickListener(this)

        helper = JobDetailHelper(findViewById<View>(android.R.id.content))

        // get job from intent
        val bundle = intent.extras
        this.job = bundle.getParcelable(JobDetailHelper.KEY_JOB)

        helper!!.fillJobInfo(this.job!!)

        // fill bid content if already bidded
        for (bidItem in this.job!!.bidArray) {
            if (TextUtils.equals(bidItem.userId, User.currentUser!!.id)) {
                this.edit_time.setText(bidItem.time)
                this.edit_price.setText(bidItem.price)
                this.edit_contact.setText(bidItem.contact)

                this.bid = bidItem

                break
            }
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            // submit button
            R.id.but_submit -> {
                submitBid()
            }
        }
    }

    private fun submitBid() {
        val strTime = this.edit_time.text.toString()
        val strPrice = this.edit_price.text.toString()
        val strContact = this.edit_contact.text.toString()

        if (TextUtils.isEmpty(strTime)) {
            Utils.createErrorAlertDialog(this,
                    resources.getString(R.string.error_fill_data),
                    "Estimated time cannot be empty",
                    DialogInterface.OnClickListener { dialog, which ->
                        this.edit_time.requestFocus()
                    }
            ).show()
            return
        }
        if (TextUtils.isEmpty(strPrice)) {
            Utils.createErrorAlertDialog(this,
                    resources.getString(R.string.error_fill_data),
                    "Price cannot be empty",
                    DialogInterface.OnClickListener { dialog, which ->
                        this.edit_price.requestFocus()
                    }
            ).show()
            return
        }
        if (TextUtils.isEmpty(strContact)) {
            Utils.createErrorAlertDialog(this,
                    resources.getString(R.string.error_fill_data),
                    "Contact information cannot be empty",
                    DialogInterface.OnClickListener { dialog, which ->
                        this.edit_contact.requestFocus()
                    }
            ).show()
            return
        }

        // submit
        var newBid = this.bid
        if (newBid == null) {
            newBid = Bid()

            // generate id
            val database = FirebaseDatabase.getInstance().reference
            val strKey = database.child(Bid.TABLE_NAME).push().getKey();
            newBid.id = strKey

            newBid.jobId = job!!.id
            newBid.userId = User.currentUser!!.id

            this.job!!.bidArray.add(newBid)
        }

        newBid.time = strTime
        newBid.price = strPrice
        newBid.contact = strContact

        newBid.saveToDatabase(newBid.id)

        finish()
    }
}
