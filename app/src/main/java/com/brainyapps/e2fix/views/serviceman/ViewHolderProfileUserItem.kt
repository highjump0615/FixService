package com.brainyapps.e2fix.views.serviceman

import android.content.Context
import android.text.TextUtils
import android.view.View
import com.brainyapps.e2fix.activities.UserDetailHelper
import com.brainyapps.e2fix.models.User
import com.brainyapps.e2fix.utils.Utils
import com.brainyapps.e2fix.views.admin.ViewHolderBase
import kotlinx.android.synthetic.main.layout_content_serviceman_setting.view.*
import kotlinx.android.synthetic.main.layout_sm_profile_info_item.view.*

/**
 * Created by Administrator on 2/19/18.
 */

class ViewHolderProfileUserItem(itemView: View, ctx: Context) : ViewHolderBase(itemView, ctx) {

    var helper: UserDetailHelper? = null

    init {
        this.helper = UserDetailHelper(itemView)
    }

    fun fillContent(data: User) {
        this.helper!!.fillUserInfo(data)

        // rate
        itemView.text_rate.text = "${data.rating}/5.0"

        // rate
        itemView.ratestar.updateStar(data.rating)

        // empty notice
        if (data.reviews.isEmpty()) {
            itemView.text_empty_notice.visibility = View.VISIBLE
        }

        // rate
        itemView.text_rate.text = String.format("%.1f/5.0", data.rating)
        itemView.ratestar.updateStar(data.rating)

        // write review
        if (!TextUtils.equals(data.id, User.currentUser!!.id)) {
            itemView.layout_rate_edit.visibility = View.VISIBLE
            itemView.text_empty_notice.visibility = View.GONE

            itemView.ratestar_edit.rateListener = object : ViewRateStar.SelectRateListener {
                override fun onSelectRate(rate: Double) {
                    itemView.text_rate_edit.text = String.format("%.1f", rate)
                }
            }
        }
    }

    fun clearReviewEdit() {
        itemView.ratestar_edit.updateStar(0.0)
        itemView.text_rate_edit.text = "0.0"
        itemView.edit_review.setText("")
    }
}
