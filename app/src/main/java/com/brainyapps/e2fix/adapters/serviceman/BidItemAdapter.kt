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
import com.brainyapps.e2fix.models.Bid
import com.brainyapps.e2fix.utils.Utils
import com.brainyapps.e2fix.views.serviceman.ViewHolderBidItem
import com.brainyapps.e2fix.views.serviceman.ViewHolderHeader
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Administrator on 2/19/18.
 */

class BidItemAdapter(val ctx: Context, private val aryBid: ArrayList<Bid>, val type: Int)
    : BaseItemAdapter(ctx) {

    val formatNew = SimpleDateFormat("yyyy-MM-dd")

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
        //
        // calculate bid item
        //
        var dateTemp: Date = formatNew.parse("2018-01-01")
        var nIndex = 0
        var nDateIndex = 0

        for (bidItem in aryBid) {
            if (!Utils.equalDate(dateTemp, bidItem.dateCreated!!)) {
                dateTemp = bidItem.dateCreated!!

                // update view
                if (holder is ViewHolderHeader) {
                    holder.fillContent(dateTemp)
                }

                nIndex++
                nDateIndex++
            }

            if (holder is ViewHolderBidItem) {
                // update view
                if (nIndex == position) {
                    holder.fillContent(aryBid[position - nDateIndex])
                }
            }
            nIndex++
        }
    }

    override fun getItemCount(): Int {
        var nCount = getCount()

        if (mbNeedMore) {
            nCount++
        }

        return nCount
    }

    override fun getItemViewType(position: Int): Int {
        //
        // calculate data index
        //
        var viewType = ITEM_VIEW_TYPE_FOOTER
        var dateTemp: Date = formatNew.parse("2018-01-01")
        var nIndex = 0

        for (jobItem in aryBid) {
            if (!Utils.equalDate(dateTemp, jobItem.dateCreated!!)) {
                dateTemp = jobItem.dateCreated!!

                if (nIndex == position) {
                    viewType = ITEM_VIEW_TYPE_HEADER
                }
                nIndex++
            }

            if (nIndex == position) {
                viewType = ITEM_VIEW_TYPE_JOB
            }
            nIndex++
        }

        return viewType
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

    /**
     * get list item count
     */
    fun getCount(): Int {
        var nDateCount = 0
        var dateTemp: Date = formatNew.parse("2018-01-01")

        for (jobItem in aryBid) {
            if (!Utils.equalDate(dateTemp, jobItem.dateCreated!!)) {
                dateTemp = jobItem.dateCreated!!
                nDateCount++
            }
        }

        return nDateCount + aryBid.count()
    }
}
