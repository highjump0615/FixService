package com.brainyapps.ezfix.adapters.serviceman

import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.brainyapps.ezfix.R
import com.brainyapps.ezfix.activities.ReportHelper
import com.brainyapps.ezfix.adapters.BaseItemAdapter
import com.brainyapps.ezfix.models.Review
import com.brainyapps.ezfix.models.User
import com.brainyapps.ezfix.utils.Utils
import com.brainyapps.ezfix.views.admin.ViewHolderUserItem
import com.brainyapps.ezfix.views.serviceman.ViewHolderProfileUserItem
import com.brainyapps.ezfix.views.serviceman.ViewHolderReviewItem
import com.brainyapps.ezfix.views.serviceman.ViewRateStar
import kotlinx.android.synthetic.main.layout_sm_profile_info_item.view.*

/**
 * Created by Administrator on 2/19/18.
 */

class ProfileAdapter(val ctx: Context, var user: User) : BaseItemAdapter(ctx) {

    companion object {
        val ITEM_VIEW_TYPE_USER = 0
        val ITEM_VIEW_TYPE_REVIEW = 1
    }

    private var reportHelper: ReportHelper? = null

    init {
        this.reportHelper = ReportHelper(context as Activity)
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

            // write review
            holder.itemView.imgview_review_submit.setOnClickListener(View.OnClickListener {
                // submit rate
                val strReview = holder.itemView.edit_review.text.toString()
                val dRate = holder.itemView.text_rate_edit.text.toString().toDouble()
                if (!TextUtils.isEmpty(strReview)) {
                    // add review
                    val newReview = Review()

                    newReview.userId = User.currentUser!!.id
                    newReview.user = User.currentUser
                    newReview.userRatedId = user.id
                    newReview.userRated = user

                    newReview.rate = dRate
                    newReview.review = strReview

                    newReview.saveToDatabase()

                    // calculate average user rate
                    val dSum = user.rating * user.reviews.count() + dRate
                    user.rating = dSum / (user.reviews.count() + 1)
                    user.saveToDatabase()

                    // clear edittext
                    holder.clearReviewEdit()

                    // update review list
                    user.reviews.add(0, newReview)
                    notifyItemChanged(0)
                    notifyItemInserted(1)
                }

                holder.itemView.edit_review.clearFocus()
                Utils.hideKeyboard(this.context!!)
            })
        }
        else if (holder is ViewHolderReviewItem) {
            holder.fillContent(user.reviews[position - 1])
        }
    }

    override fun getItemCount(): Int {
        var nCount = 1

        // adds reviews with user info only
        for (reviewItem in user.reviews) {
            reviewItem.user?.let {
                nCount++
            }
        }

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
        when (view?.id) {
            // report icon
            R.id.imgview_report -> {
                this.reportHelper!!.addReport(user.reviews[position - 1].userId)
            }
        }
    }


}
