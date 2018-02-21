package com.brainyapps.e2fix.views.serviceman

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.brainyapps.e2fix.R
import kotlinx.android.synthetic.main.app_bar_serviceman.view.*

/**
 * Created by Administrator on 2/19/18.
 */
class ViewRateStar : LinearLayout {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    fun init() {
        View.inflate(context, R.layout.layout_star_rate, this)
    }
}
