package com.brainyapps.e2fix.adapters.serviceman

import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.adapters.BaseItemAdapter
import com.brainyapps.e2fix.models.User
import com.brainyapps.e2fix.views.admin.ViewHolderUserItem
import com.brainyapps.e2fix.views.serviceman.ViewHolderProfileUserItem
import com.brainyapps.e2fix.views.serviceman.ViewHolderReviewItem

/**
 * Created by Administrator on 2/19/18.
 */

class ProfileAdapter(val ctx: Context, var user: User) : BaseItemAdapter(ctx) {

    companion object {
        val ITEM_VIEW_TYPE_USER = 0
        val ITEM_VIEW_TYPE_REVIEW = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        var vhRes = super.onCreateViewHolder(parent, viewType)

        if (vhRes == null) {
            if (viewType == ITEM_VIEW_TYPE_USER) {
                // create a new view
                val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_sm_profile_info_item, parent, false)
                // set the view's size, margins, paddings and layout parameters

                val vh = ViewHolderProfileUserItem(v, ctx)
                vh.setOnItemClickListener(this)
                vhRes = vh
            }
            else if (viewType == ITEM_VIEW_TYPE_REVIEW) {
                // create a new view
                val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_serviceman_rating_list_item, parent, false)
                // set the view's size, margins, paddings and layout parameters

                val vh = ViewHolderReviewItem(v, ctx)
                vh.setOnItemClickListener(this)
                vhRes = vh
            }
        }

        return vhRes!!
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder is ViewHolderProfileUserItem) {
            holder.fillContent(user)
        }
        else {
        }
    }

    override fun getItemCount(): Int {
        var nCount = user.reviews.size + 1

        if (mbNeedMore) {
            nCount++
        }

        return nCount
    }

    override fun getItemViewType(position: Int): Int {

        return if (position == 0) {
            ITEM_VIEW_TYPE_USER
        }
        else if (position < user.reviews.size + 1) {
            ITEM_VIEW_TYPE_REVIEW
        }
        else {
            ITEM_VIEW_TYPE_FOOTER
        }
    }

    override fun onItemClick(view: View?, position: Int) {
    }
}
