package com.brainyapps.e2fix.views.serviceman

import android.support.v7.widget.RecyclerView
import android.view.View
import com.brainyapps.e2fix.utils.Utils
import kotlinx.android.synthetic.main.layout_bid_list_header_item.view.*
import java.util.*

/**
 * Created by Administrator on 2/19/18.
 */
class ViewHolderHeader(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun fillContent(date: Date) {
        //
        // Show formatted date
        //
        itemView.text_date.text = Utils.getFormattedDate(date)
    }
}
