package com.brainyapps.e2fix.adapters.serviceman

import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.activities.serviceman.BidSubmitActivity
import com.brainyapps.e2fix.adapters.BaseItemAdapter
import com.brainyapps.e2fix.models.Job
import com.brainyapps.e2fix.models.Review
import com.brainyapps.e2fix.utils.E2FItemClickListener
import com.brainyapps.e2fix.utils.Utils
import com.brainyapps.e2fix.views.ViewHolderLoading
import com.brainyapps.e2fix.views.admin.ViewHolderUserItem
import com.brainyapps.e2fix.views.serviceman.ViewHolderJobItem
import com.brainyapps.e2fix.views.serviceman.ViewHolderProfileUserItem
import com.brainyapps.e2fix.views.serviceman.ViewHolderReviewItem
import com.bumptech.glide.util.Util
import java.util.ArrayList

/**
 * Created by Administrator on 2/19/18.
 */

class ProfileAdapter(val ctx: Context, val aryReview: ArrayList<Review>)
    : BaseItemAdapter() {

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
        if (holder is ViewHolderUserItem) {
        }
        else {
        }
    }

    override fun getItemCount(): Int {
        var nCount = aryReview.size + 1

        if (mbNeedMore) {
            nCount++
        }

        return nCount
    }

    override fun getItemViewType(position: Int): Int {

        return if (position == 0) {
            ITEM_VIEW_TYPE_USER
        }
        else if (position < aryReview.size + 1) {
            ITEM_VIEW_TYPE_REVIEW
        }
        else {
            ITEM_VIEW_TYPE_FOOTER
        }
    }

    override fun onItemClick(view: View?, position: Int) {
        when (view?.id) {
            // bid button
            R.id.but_bid -> {
                Utils.moveNextActivity(ctx as Activity, BidSubmitActivity::class.java)
            }
        }
    }
}
