package com.brainyapps.e2fix.views.serviceman

import android.content.Context
import android.support.v7.widget.CardView
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.activities.serviceman.BidActivity
import com.brainyapps.e2fix.views.admin.ViewHolderBase
import kotlinx.android.synthetic.main.layout_bid_list_item.view.*

/**
 * Created by Administrator on 2/19/18.
 */

class ViewHolderBidItem(itemView: View, ctx: Context, type: Int) : ViewHolderBase(itemView, ctx) {

    init {
        val viewMain = itemView.findViewById<CardView>(R.id.view_main)
        viewMain.setOnClickListener(this)

        if (type == BidActivity.TYPE_BID_WON) {
            itemView.layout_bid.visibility = View.GONE
        }
    }

    fun setNewMarkVisible(visibility: Int) {
        itemView.text_banner.visibility = visibility
    }
}
