package com.brainyapps.ezfix.views.serviceman

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.brainyapps.ezfix.R
import com.brainyapps.ezfix.activities.JobDetailHelper
import com.brainyapps.ezfix.models.Bid
import com.brainyapps.ezfix.models.Job
import com.brainyapps.ezfix.models.User
import com.brainyapps.ezfix.utils.Utils
import com.brainyapps.ezfix.views.admin.ViewHolderBase
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.layout_job_list_item.view.*

/**
 * Created by Administrator on 2/19/18.
 */

class ViewHolderJobItem(itemView: View, ctx: Context) : ViewHolderBase(itemView, ctx) {

    var helper: JobDetailHelper? = null

    init {
        val viewMain = itemView.findViewById<CardView>(R.id.view_main)
        viewMain.setOnClickListener(this)

        itemView.but_bid.setOnClickListener(this)

        helper = JobDetailHelper(itemView)
    }

    fun fillContent(data: Job) {
        helper!!.fillJobInfo(data)

        // bid
        itemView.imgview_bid_icon.setColorFilter(ContextCompat.getColor(context!!, R.color.colorTheme))
        itemView.text_job_bid.setTextColor(itemView.text_job_location.textColors)

        val bid = data.jobBidTaken()
        if (bid != null) {
            // fetch user if not fetched
            if (bid.user != null) {
                updateBidText(bid)
            }
            else {
                User.readFromDatabase(bid.userId, object : User.FetchDatabaseListener {
                    override fun onFetchedReviews() {
                    }

                    override fun onFetchedUser(user: User?, success: Boolean) {
                        bid.user = user

                        updateBidText(bid)
                    }
                })
            }
        }

        // check if posted job
        if (TextUtils.equals(data.userId, User.currentUser!!.id)) {
            itemView.but_bid.setText("VIEW BIDS")
        }
        else {
            itemView.but_bid.setText("BID")
        }
    }

    fun updateBidText(bid: Bid) {
        itemView.text_job_bid.text = bid.user!!.userFullName()
        itemView.imgview_bid_icon.setColorFilter(ContextCompat.getColor(context!!, R.color.colorGoogle))
        itemView.text_job_bid.setTextColor(ContextCompat.getColor(context!!, R.color.colorGoogle))
    }
}
