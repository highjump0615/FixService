package com.brainyapps.e2fix.views.admin

import android.content.Context
import android.support.v7.widget.CardView
import android.view.View
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.models.User
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.layout_user_list_item.view.*

/**
 * Created by Administrator on 2/19/18.
 */

class ViewHolderUserItem(itemView: View, ctx: Context) : ViewHolderBase(itemView, ctx) {

    init {
        val viewMain = itemView.findViewById<CardView>(R.id.view_main)
        viewMain.setOnClickListener(this)
    }

    fun fillContent(data: User) {
        // photo
        Glide.with(this.context!!)
                .load(data.photoUrl)
                .apply(RequestOptions.placeholderOf(R.drawable.user_default).fitCenter())
                .into(itemView.imgview_photo)

        // name
        itemView.text_name.text = data.userFullName()

        // check if posted job
        itemView.text_type.text = data.userTypeString()
    }

    fun showReportMark() {
        itemView.imgview_flag.visibility = View.VISIBLE
    }

}
