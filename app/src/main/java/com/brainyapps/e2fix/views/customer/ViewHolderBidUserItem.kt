package com.brainyapps.e2fix.views.customer

import android.content.Context
import android.support.v7.widget.CardView
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.activities.UserDetailHelper
import com.brainyapps.e2fix.activities.serviceman.BidActivity
import com.brainyapps.e2fix.models.Bid
import com.brainyapps.e2fix.views.admin.ViewHolderBase
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.layout_customer_bid_list_item.view.*

/**
 * Created by Administrator on 2/19/18.
 */

class ViewHolderBidUserItem(itemView: View, ctx: Context) : ViewHolderBase(itemView, ctx) {

    var helper: UserDetailHelper? = null

    init {
        val viewMain = itemView.findViewById<LinearLayout>(R.id.view_main)
        viewMain.setOnClickListener(this)

        this.helper = UserDetailHelper(itemView)
    }

    fun fillContent(data: Bid) {
        this.helper!!.fillUserInfo(data.user!!)

        // bid info
        itemView.text_bid_time.text = data.time
        itemView.text_bid_price.text = "$${data.price}"
        itemView.text_bid_contact.text = data.contact
    }
}
