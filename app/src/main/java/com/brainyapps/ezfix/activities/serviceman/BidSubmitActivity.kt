package com.brainyapps.ezfix.activities.serviceman

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.brainyapps.ezfix.R
import com.brainyapps.ezfix.activities.BaseActivity
import com.brainyapps.ezfix.activities.JobDetailHelper
import com.brainyapps.ezfix.activities.GeoLocationHelper
import com.brainyapps.ezfix.activities.ReportHelper
import com.brainyapps.ezfix.models.Bid
import com.brainyapps.ezfix.models.Job
import com.brainyapps.ezfix.models.Report
import com.brainyapps.ezfix.models.User
import com.brainyapps.ezfix.utils.CommonObjects
import com.brainyapps.ezfix.utils.Utils
import com.firebase.geofire.GeoLocation
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_bid_submit.*
import kotlinx.android.synthetic.main.app_bar_serviceman.*
import kotlinx.android.synthetic.main.app_bar_serviceman.view.*

class BidSubmitActivity : BaseActivity(), View.OnClickListener {

    private var jobHelper: JobDetailHelper? = null
    private var locationHelper: GeoLocationHelper? = null
    private var reportHelper: ReportHelper? = null

    var job: Job? = null
    var bid: Bid? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bid_submit)

        setNavbar(null, true)

        this.but_submit.setOnClickListener(this)

        jobHelper = JobDetailHelper(findViewById<View>(android.R.id.content))

        // right toolbar menu
        this.toolbar.imgview_right.setOnClickListener(this)

        // get job from common object
        this.job = CommonObjects.selectedJob!!

        jobHelper!!.fillJobInfo(this.job!!)

        // fill bid content if already bidded
        for (bidItem in this.job!!.bidArray) {
            if (TextUtils.equals(bidItem.userId, User.currentUser!!.id)) {
                this.edit_time.setText(bidItem.time)
                this.edit_price.setText(bidItem.price.toString())
                this.edit_contact.setText(bidItem.contact)

                this.bid = bidItem

                break
            }
        }

        // init location
        this.locationHelper = GeoLocationHelper(this, "Bid submit needs location for showing distance")

        this.reportHelper = ReportHelper(this)

        // submit button
        enableButton(this.but_submit, this.job!!.isBidAvailable())
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            // submit button
            R.id.but_submit -> {
                submitBid()
            }

            // report
            R.id.imgview_right -> {
                reportHelper!!.addReport(this.job!!.userId)
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

            newBid.jobId = job!!.id
            newBid.userId = User.currentUser!!.id

            this.job!!.bidArray.add(newBid)
        }

        newBid.time = strTime
        newBid.price = strPrice.toDouble()
        newBid.contact = strContact

        this.locationHelper!!.location?.let {
            newBid!!.latitude = it.latitude
            newBid!!.longitude = it.longitude
        }

        newBid.saveToDatabase()

        finish()
    }
}
