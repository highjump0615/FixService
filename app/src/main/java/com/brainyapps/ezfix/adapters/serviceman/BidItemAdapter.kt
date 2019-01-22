package com.brainyapps.ezfix.adapters.serviceman

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.brainyapps.ezfix.R
import com.brainyapps.ezfix.activities.JobDetailHelper
import com.brainyapps.ezfix.activities.serviceman.BidDetailActivity
import com.brainyapps.ezfix.activities.serviceman.BidSubmitActivity
import com.brainyapps.ezfix.adapters.BaseItemAdapter
import com.brainyapps.ezfix.models.Bid
import com.brainyapps.ezfix.utils.Utils
import com.brainyapps.ezfix.views.serviceman.ViewHolderBidItem
import com.brainyapps.ezfix.views.serviceman.ViewHolderHeader
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Administrator on 2/19/18.
 */

class BidItemAdapter(val ctx: Context, private val aryBid: ArrayList<Bid>, val type: Int)
    : BaseItemAdapter(ctx) {

    val formatNew = SimpleDateFormat("yyyy-MM-dd")
    val ACTION_GOTO_DETAIL = 1

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

    private fun handleItemViewHolder(holder: RecyclerView.ViewHolder?, position: Int, action: Int = 0) {
        //
        // calculate bid item
        //
        var dateTemp: Date = formatNew.parse("2018-01-01")
        var nIndex = 0
        var nDateIndex = 0

        for (bidItem in aryBid) {
            val dtCreatedAt = Date(bidItem.createdAt)
            if (!Utils.equalDate(dateTemp, dtCreatedAt)) {
                dateTemp = dtCreatedAt

                // update view
                if (nIndex == position) {
                    if (holder is ViewHolderHeader) {
                        holder.fillContent(dateTemp)
                    }
                }

                nIndex++
                nDateIndex++
            }

            if (nIndex == position) {
                // update view
                if (holder is ViewHolderBidItem) {
                    holder.fillContent(aryBid[position - nDateIndex])
                }
                else if (action == ACTION_GOTO_DETAIL) {
                    val intent = Intent(ctx, BidDetailActivity::class.java)
                    intent.putExtra(BidDetailActivity.KEY_BID, aryBid[position - nDateIndex])
                    intent.putExtra(JobDetailHelper.KEY_JOB, aryBid[position - nDateIndex].job)
                    ctx.startActivity(intent)
                }
            }

            nIndex++
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        handleItemViewHolder(holder, position)
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
            val dtCreatedAt = Date(jobItem.createdAt)
            if (!Utils.equalDate(dateTemp, dtCreatedAt)) {
                dateTemp = dtCreatedAt

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
                handleItemViewHolder(null, position, ACTION_GOTO_DETAIL)
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
            val dtCreatedAt = Date(jobItem.createdAt)
            if (!Utils.equalDate(dateTemp, dtCreatedAt)) {
                dateTemp = dtCreatedAt
                nDateCount++
            }
        }

        return nDateCount + aryBid.count()
    }
}
