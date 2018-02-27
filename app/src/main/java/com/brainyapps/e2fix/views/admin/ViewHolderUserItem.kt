package com.brainyapps.e2fix.views.admin

import android.content.Context
import android.support.v7.widget.CardView
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.models.User
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_signup_info.*
import kotlinx.android.synthetic.main.layout_user_list_item.view.*

/**
 * Created by Administrator on 2/19/18.
 */

class ViewHolderUserItem(itemView: View, ctx: Context, showReport: Boolean) : ViewHolderBase(itemView) {

    var context: Context? = null

    init {
        this.context = ctx
        val viewMain = itemView.findViewById<CardView>(R.id.view_main)
        viewMain.setOnClickListener(this)

        if (showReport) {
            val imgFlag = itemView.findViewById<ImageView>(R.id.imgview_flag)
            imgFlag.visibility = View.VISIBLE
        }
    }

    fun fillContent(data: User) {
        // photo
        Glide.with(this.context!!)
                .load(User.currentUser?.photoUrl)
                .apply(RequestOptions.placeholderOf(R.drawable.user_default).fitCenter())
                .into(itemView.imgview_photo)

        // name
        itemView.text_name.setText("${data.firstName} ${data.lastName}", TextView.BufferType.EDITABLE)

        // check if posted job
        itemView.text_type.setText(data.getTypeString(), TextView.BufferType.EDITABLE)
    }

}
