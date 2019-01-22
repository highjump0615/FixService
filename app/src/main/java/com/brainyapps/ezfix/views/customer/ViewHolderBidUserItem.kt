package com.brainyapps.ezfix.views.customer

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import com.brainyapps.ezfix.R
import com.brainyapps.ezfix.activities.BaseActivity
import com.brainyapps.ezfix.activities.UserDetailHelper
import com.brainyapps.ezfix.activities.customer.BidDetailActivity
import com.brainyapps.ezfix.models.Bid
import com.brainyapps.ezfix.utils.Utils
import com.brainyapps.ezfix.views.admin.ViewHolderBase
import com.firebase.geofire.GeoLocation
import kotlinx.android.synthetic.main.layout_customer_bid_list_item.view.*

/**
 * Created by Administrator on 2/19/18.
 */

class ViewHolderBidUserItem(itemView: View, ctx: Context) : ViewHolderBase(itemView, ctx) {

    var helper: UserDetailHelper? = null

    init {
        val viewMain = itemView.findViewById<LinearLayout>(R.id.view_main)
        viewMain.setOnClickListener(this)

        itemView.but_choose_bidder.setOnClickListener(this)

        this.helper = UserDetailHelper(itemView)
    }

    fun fillContent(data: Bid, bidAvailable: Boolean) {
        this.helper!!.fillUserInfo(data.user!!)

        // distance
        val activity = itemView.context as BidDetailActivity
        var dDistance = -1.0;
        activity.locationHelper!!.location?.let {
            dDistance = Utils.distanceInMilesTo(GeoLocation(it.latitude, it.longitude), data.bidGeoLocation())
        }

        if (dDistance >= 0) {
            itemView.text_location.text = String.format("%.1f miles away from you", dDistance)
        }

        // bid info
        itemView.text_bid_time.text = data.time
        itemView.text_bid_price.text = "$${data.price}"
        itemView.text_bid_contact.text = data.contact

        // choose bidder button
        if (data.isTaken) {
            itemView.but_choose_bidder.text = "BID WINNER"
        }
        else {
            itemView.but_choose_bidder.text = "CHOOSE AS THE BID WINNER"
        }

        // button enable status
        activity.enableButton(itemView.but_choose_bidder, bidAvailable)
    }
}
