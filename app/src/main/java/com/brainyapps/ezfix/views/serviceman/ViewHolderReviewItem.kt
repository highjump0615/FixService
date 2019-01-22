package com.brainyapps.ezfix.views.serviceman

import android.content.Context
import android.support.v7.widget.CardView
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.brainyapps.ezfix.R
import com.brainyapps.ezfix.models.Review
import com.brainyapps.ezfix.utils.Utils
import com.brainyapps.ezfix.views.admin.ViewHolderBase
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.layout_serviceman_rating_list_item.view.*
import java.util.*

/**
 * Created by Administrator on 2/19/18.
 */

class ViewHolderReviewItem(itemView: View, ctx: Context) : ViewHolderBase(itemView, ctx) {

    init {
        itemView.imgview_report.setOnClickListener(this)
    }

    fun fillContent(data: Review) {
        val user = data.user!!

        // photo
        Glide.with(this.context!!)
                .load(user.photoUrl)
                .apply(RequestOptions.placeholderOf(R.drawable.job_default).fitCenter())
                .into(itemView.imgview_user_photo)

        // name
        itemView.text_name.text = user.userFullName()

        // time
        itemView.text_time.text = Utils.getFormattedDateTime(Date(data.createdAt))

        // star
        itemView.ratestar.updateStar(data.rate)
        itemView.text_rate.text = String.format("%.1f/5.0", data.rate)

        // review
        itemView.text_review.text = data.review
    }
}
