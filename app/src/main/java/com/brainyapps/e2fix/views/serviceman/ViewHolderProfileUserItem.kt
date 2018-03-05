package com.brainyapps.e2fix.views.serviceman

import android.content.Context
import android.view.View
import com.brainyapps.e2fix.activities.UserDetailHelper
import com.brainyapps.e2fix.models.User
import com.brainyapps.e2fix.views.admin.ViewHolderBase
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
        itemView.text_empty_notice.visibility = View.VISIBLE
    }
}
