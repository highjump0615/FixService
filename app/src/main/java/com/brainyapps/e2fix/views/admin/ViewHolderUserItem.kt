package com.brainyapps.e2fix.views.admin

import android.content.Context
import android.support.v7.widget.CardView
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.brainyapps.e2fix.R

/**
 * Created by Administrator on 2/19/18.
 */

class ViewHolderUserItem(itemView: View, ctx: Context) : ViewHolderBase(itemView) {

    init {
        val viewMain = itemView.findViewById<CardView>(R.id.view_main)
        viewMain.setOnClickListener(this)
    }

}
