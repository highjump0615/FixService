package com.brainyapps.ezfix.adapters.customer

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.brainyapps.ezfix.models.Bid
import com.brainyapps.ezfix.R
import com.brainyapps.ezfix.activities.UserDetailHelper
import com.brainyapps.ezfix.activities.customer.BidDetailActivity
import com.brainyapps.ezfix.activities.customer.BidPayActivity
import com.brainyapps.ezfix.activities.customer.BidderProfileActivity
import com.brainyapps.ezfix.activities.serviceman.*
import com.brainyapps.ezfix.adapters.BaseItemAdapter
import com.brainyapps.ezfix.models.Job
import com.brainyapps.ezfix.utils.CommonObjects
import com.brainyapps.ezfix.utils.Utils
import com.brainyapps.ezfix.views.customer.ViewHolderBidUserItem
import com.brainyapps.ezfix.views.serviceman.ViewHolderBidItem
import java.util.ArrayList

/**
 * Created by Administrator on 2/19/18.
 */

class BidItemAdapter(ctx: Context) : BaseItemAdapter(ctx) {

    var job: Job? = null

    companion object {
        val ITEM_VIEW_TYPE_JOB = 0
        val ITEM_VIEW_TYPE_BID = 1
    }

    init {
        val activity = ctx as BidDetailActivity
        this.job = activity.job
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        var vhRes = super.onCreateViewHolder(parent, viewType)

        if (vhRes == null) {
            if (viewType == ITEM_VIEW_TYPE_JOB) {
                // create a new view
                val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_bid_list_item, parent, false)
                // set the view's size, margins, paddings and layout parameters

                val vh = ViewHolderBidItem(v, this.context!!, BidActivity.TYPE_JOB_APPLIED)
                vhRes = vh
            }
            else if (viewType == ITEM_VIEW_TYPE_BID) {
                // create a new view
                val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_customer_bid_list_item, parent, false)
                // set the view's size, margins, paddings and layout parameters

                val vh = ViewHolderBidUserItem(v, this.context!!)
                vh.setOnItemClickListener(this)
                vhRes = vh
            }
        }

        return vhRes!!
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder is ViewHolderBidItem) {
            holder.fillContent(this.job!!)
        }
        else if (holder is ViewHolderBidUserItem) {
            holder.fillContent(this.job!!.bidArray[position - 1], TextUtils.isEmpty(this.job!!.bidTakenId))
        }
    }

    override fun getItemCount(): Int {
        var nCount = 1

        // adds user-fetched bids only
        for (bidItem in this.job!!.bidArray) {
            if (bidItem.user != null) {
                nCount++
            }
        }

        if (mbNeedMore) {
            nCount++
        }

        return nCount
    }

    override fun getItemViewType(position: Int): Int {

        return if (position == 0) {
            ITEM_VIEW_TYPE_JOB
        }
        else if (position < this.job!!.bidArray.size + 1) {
            ITEM_VIEW_TYPE_BID
        }
        else {
            ITEM_VIEW_TYPE_FOOTER
        }
    }

    override fun onItemClick(view: View?, position: Int) {
        val bid = this.job!!.bidArray[position - 1]

        when (view?.id) {
            R.id.view_main -> {
                val intent = Intent(this.context, BidderProfileActivity::class.java)
                intent.putExtra(UserDetailHelper.KEY_USER, bid.user)
                this.context!!.startActivity(intent)
            }

            // choose as bidder -> {
            R.id.but_choose_bidder -> {
                // go to payment page
                CommonObjects.selectedJob = this.job
                CommonObjects.selectedBid = bid
                Utils.moveNextActivity(this.context as Activity, BidPayActivity::class.java)
            }
        }
    }
}
