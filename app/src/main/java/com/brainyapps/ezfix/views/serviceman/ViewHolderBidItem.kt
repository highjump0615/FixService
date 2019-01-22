package com.brainyapps.ezfix.views.serviceman

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.text.TextUtils
import android.view.View
import com.brainyapps.ezfix.R
import com.brainyapps.ezfix.activities.JobDetailHelper
import com.brainyapps.ezfix.activities.serviceman.BidActivity
import com.brainyapps.ezfix.models.Bid
import com.brainyapps.ezfix.models.Job
import com.brainyapps.ezfix.views.admin.ViewHolderBase
import kotlinx.android.synthetic.main.layout_bid_list_item.view.*

/**
 * Created by Administrator on 2/19/18.
 */

class ViewHolderBidItem(itemView: View, ctx: Context, type: Int) : ViewHolderBase(itemView, ctx) {

    var helper: JobDetailHelper? = null

    init {
        val viewMain = itemView.findViewById<CardView>(R.id.view_main)
        viewMain.setOnClickListener(this)

        if (type == BidActivity.TYPE_BID_WON) {
            itemView.layout_bid.visibility = View.GONE
        }

        helper = JobDetailHelper(itemView)
    }

    fun setNewMarkVisible(visibility: Int) {
        itemView.text_banner.visibility = visibility
    }

    fun fillContent(job: Job) {
        helper!!.fillJobInfo(job)
    }

    fun fillContent(data: Bid) {
        helper!!.fillJobInfo(data.job!!)

        // bid
        if (TextUtils.isEmpty(data.job!!.bidTakenId)) {
            itemView.text_job_bid.text = "Waiting for a Bid winners"

            // color
            itemView.text_job_bid.setTextColor(ContextCompat.getColor(context!!, R.color.colorTheme))
            itemView.imgview_bid_icon.setColorFilter(ContextCompat.getColor(context!!, R.color.colorTheme))
        }
        else if (TextUtils.equals(data.job!!.bidTakenId, data.id)) {
            itemView.text_job_bid.text = "You got this job!"

            // color
            itemView.text_job_bid.setTextColor(ContextCompat.getColor(context!!, R.color.colorGoogle))
            itemView.imgview_bid_icon.setColorFilter(ContextCompat.getColor(context!!, R.color.colorGoogle))
        }
        else {
            itemView.text_job_bid.text = "Job taken"

            // color
            itemView.text_job_bid.setTextColor(ContextCompat.getColor(context!!, R.color.colorGrey))
            itemView.imgview_bid_icon.setColorFilter(ContextCompat.getColor(context!!, R.color.colorGrey))
        }
    }
}
