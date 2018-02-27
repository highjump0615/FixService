package com.brainyapps.e2fix.adapters.customer

import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.brainyapps.e2fix.models.Bid
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.activities.customer.BidderProfileActivity
import com.brainyapps.e2fix.activities.serviceman.*
import com.brainyapps.e2fix.adapters.BaseItemAdapter
import com.brainyapps.e2fix.models.Job
import com.brainyapps.e2fix.utils.Utils
import com.brainyapps.e2fix.views.customer.ViewHolderBidUserItem
import com.brainyapps.e2fix.views.serviceman.ViewHolderBidItem
import java.util.ArrayList

/**
 * Created by Administrator on 2/19/18.
 */

class BidItemAdapter(val ctx: Context, val aryBid: ArrayList<Bid>)
    : BaseItemAdapter(ctx) {

    companion object {
        val ITEM_VIEW_TYPE_JOB = 0
        val ITEM_VIEW_TYPE_BID = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        var vhRes = super.onCreateViewHolder(parent, viewType)

        if (vhRes == null) {
            if (viewType == ITEM_VIEW_TYPE_JOB) {
                // create a new view
                val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_bid_list_item, parent, false)
                // set the view's size, margins, paddings and layout parameters

                val vh = ViewHolderBidItem(v, ctx, BidActivity.TYPE_JOB_APPLIED)
                vhRes = vh
            }
            else if (viewType == ITEM_VIEW_TYPE_BID) {
                // create a new view
                val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_customer_bid_list_item, parent, false)
                // set the view's size, margins, paddings and layout parameters

                val vh = ViewHolderBidUserItem(v, ctx)
                vh.setOnItemClickListener(this)
                vhRes = vh
            }
        }

        return vhRes!!
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder is ViewHolderBidItem) {
        }
        else {
        }
    }

    override fun getItemCount(): Int {
        var nCount = aryBid.size + 1

        if (mbNeedMore) {
            nCount++
        }

        return nCount
    }

    override fun getItemViewType(position: Int): Int {

        return if (position == 0) {
            ITEM_VIEW_TYPE_JOB
        } else if (position < aryBid.size + 1) {
            ITEM_VIEW_TYPE_BID
        }
        else {
            ITEM_VIEW_TYPE_FOOTER
        }
    }

    override fun onItemClick(view: View?, position: Int) {
        when (view?.id) {
            R.id.view_main -> {
                Utils.moveNextActivity(ctx as Activity, BidderProfileActivity::class.java)
            }
        }
    }
}
