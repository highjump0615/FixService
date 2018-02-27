package com.brainyapps.e2fix.adapters.serviceman

import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.activities.serviceman.BidActivity
import com.brainyapps.e2fix.activities.serviceman.BidDetailActivity
import com.brainyapps.e2fix.activities.serviceman.BidDetailApplyActivity
import com.brainyapps.e2fix.activities.serviceman.BidSubmitActivity
import com.brainyapps.e2fix.adapters.BaseItemAdapter
import com.brainyapps.e2fix.models.Job
import com.brainyapps.e2fix.utils.Utils
import com.brainyapps.e2fix.views.serviceman.ViewHolderBidItem
import com.brainyapps.e2fix.views.serviceman.ViewHolderHeader
import java.util.ArrayList

/**
 * Created by Administrator on 2/19/18.
 */

class BidItemAdapter(val ctx: Context, val aryUser: ArrayList<Job>, val type: Int)
    : BaseItemAdapter(ctx) {

    companion object {
        val ITEM_VIEW_TYPE_JOB = 0
        val ITEM_VIEW_TYPE_HEADER = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        var vhRes = super.onCreateViewHolder(parent, viewType)

        if (vhRes == null) {
            if (viewType == ITEM_VIEW_TYPE_HEADER) {
                val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_bid_list_header_item, parent, false)

                val vh = ViewHolderHeader(v)
                vhRes = vh
            }
            else {
                // create a new view
                val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_bid_list_item, parent, false)
                // set the view's size, margins, paddings and layout parameters

                val vh = ViewHolderBidItem(v, ctx, type)
                vh.setOnItemClickListener(this)
                vhRes = vh
            }
        }

        return vhRes
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder is ViewHolderBidItem) {
            // set new mark visibility
            if (position == 1) {
                holder.setNewMarkVisible(View.VISIBLE)
            }
            else {
                holder.setNewMarkVisible(View.INVISIBLE)
            }
        }
        else {
        }
    }

    override fun getItemCount(): Int {
        var nCount = aryUser.size

        if (mbNeedMore) {
            nCount++
        }

        return nCount
    }

    override fun getItemViewType(position: Int): Int {

        return if (position < aryUser.size) {
            if (position % 3 == 0) {
                ITEM_VIEW_TYPE_HEADER
            }
            else {
                ITEM_VIEW_TYPE_JOB
            }
        }
        else {
            ITEM_VIEW_TYPE_FOOTER
        }
    }

    override fun onItemClick(view: View?, position: Int) {
        when (view?.id) {
            R.id.view_main -> {
                if (type == BidActivity.TYPE_BID_WON) {
                    Utils.moveNextActivity(ctx as Activity, BidDetailActivity::class.java)
                }
                else if (type == BidActivity.TYPE_JOB_APPLIED) {
                    Utils.moveNextActivity(ctx as Activity, BidDetailApplyActivity::class.java)
                }
            }
            // bid button
            R.id.but_bid -> {
                Utils.moveNextActivity(ctx as Activity, BidSubmitActivity::class.java)
            }
        }
    }
}
