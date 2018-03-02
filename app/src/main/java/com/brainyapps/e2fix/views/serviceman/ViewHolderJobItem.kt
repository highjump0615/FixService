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
import com.brainyapps.e2fix.models.User
import com.brainyapps.e2fix.utils.Utils
import com.brainyapps.e2fix.views.admin.ViewHolderBase
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.layout_job_list_item.view.*

/**
 * Created by Administrator on 2/19/18.
 */

class ViewHolderJobItem(itemView: View, ctx: Context) : ViewHolderBase(itemView, ctx) {

    init {
        val viewMain = itemView.findViewById<CardView>(R.id.view_main)
        viewMain.setOnClickListener(this)

        itemView.but_bid.setOnClickListener(this)
    }

    fun fillContent(data: Job) {
        // photo
        Glide.with(this.context!!)
                .load(data.photoUrl)
                .apply(RequestOptions.placeholderOf(R.drawable.job_default).fitCenter())
                .into(itemView.imgview_photo)

        // title
        itemView.text_title.setText(data.title)

        // time
        itemView.text_time.setText(Utils.getFormattedDate(data.dateCreated!!))

        // description
        itemView.text_desc.setText(data.description)

        // location
        itemView.text_location.setText(data.userPosted?.location)

        // check if posted job
        if (TextUtils.equals(data.userId, User.currentUser!!.id)) {
            itemView.but_bid.setText("VIEW BIDS")
        }
        else {
            itemView.but_bid.setText("BID")
        }
    }
}
