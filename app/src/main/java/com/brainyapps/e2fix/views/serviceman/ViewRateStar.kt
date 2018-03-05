package com.brainyapps.e2fix.views.serviceman

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.brainyapps.e2fix.R
import kotlinx.android.synthetic.main.layout_star_rate.view.*
import java.lang.Math.round

/**
 * Created by Administrator on 2/19/18.
 */
class ViewRateStar : LinearLayout {

    var imgViewStars = ArrayList<ImageView>()

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()

        // init star image views
        imgViewStars.add(this.imgview_star1)
        imgViewStars.add(this.imgview_star2)
        imgViewStars.add(this.imgview_star3)
        imgViewStars.add(this.imgview_star4)
        imgViewStars.add(this.imgview_star5)

        val a = context.obtainStyledAttributes(attrs, R.styleable.ViewRateStar)
        val value = a.getFloat(R.styleable.ViewRateStar_starValue, 0.0F)
        updateStar(value.toDouble())

        a.recycle()
    }

    fun init() {
        View.inflate(context, R.layout.layout_star_rate, this)
    }

    public fun updateStar(value: Double) {
        for (i in 0..4) {
            if (i < round(value)) {
                imgViewStars[i].setColorFilter(ContextCompat.getColor(context!!, R.color.colorStar))
            }
            else {
                imgViewStars[i].setColorFilter(ContextCompat.getColor(context!!, R.color.colorGrey))
            }
        }
    }
}
