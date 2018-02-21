package com.brainyapps.e2fix.views.serviceman

import android.content.Context
import android.support.v7.widget.CardView
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.models.Job
import com.brainyapps.e2fix.views.admin.ViewHolderBase

/**
 * Created by Administrator on 2/19/18.
 */

class ViewHolderJobItem(itemView: View, ctx: Context) : ViewHolderBase(itemView) {

    private var butBid: Button

    init {
        val viewMain = itemView.findViewById<CardView>(R.id.view_main)
        viewMain.setOnClickListener(this)

        this.butBid = itemView.findViewById<Button>(R.id.but_bid)
        this.butBid.setOnClickListener(this)
    }

    fun fillContent(data: Job) {

        // check if posted job
        if (data.userPosted != null) {
            this.butBid.setText("VIEW BIDS")
        }
        else {
            this.butBid.setText("BID")
        }
    }
}
