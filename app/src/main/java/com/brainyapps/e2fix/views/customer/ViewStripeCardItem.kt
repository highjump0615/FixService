package com.brainyapps.e2fix.views.customer

import android.content.Context
import android.graphics.Color
import android.support.design.widget.AppBarLayout
import android.util.AttributeSet
import android.view.View
import android.support.v7.widget.Toolbar
import android.widget.RelativeLayout
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.models.StripeSource
import com.brainyapps.e2fix.utils.ImageResources
import kotlinx.android.synthetic.main.layout_payment_item.view.*

/**
 * Created by Administrator on 2/19/18.
 */
class ViewStripeCardItem : RelativeLayout {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    fun init() {
        View.inflate(context, R.layout.layout_payment_item, this)
    }

    /**
     * fill content view in item
     */
    fun fillContent(source: StripeSource) {

        this.but_add_payment.visibility = View.GONE
        this.layout_card_item.visibility = View.VISIBLE

        // brand
        val resId = ImageResources.TEMPLATE_RESOURCE_MAP[source.brand]
        this.imgview_payment_item_mcv.setImageResource(resId!!)

        // last 4
        this.text_payment_item_last4.text = "...${source.last4}"
    }
}
