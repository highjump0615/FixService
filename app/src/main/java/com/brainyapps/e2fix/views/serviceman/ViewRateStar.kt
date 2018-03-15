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
class ViewRateStar : LinearLayout, View.OnClickListener {

    private var imgViewStars = ArrayList<ImageView>()
    private var starSelectable = false

    var rateListener: SelectRateListener? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()

        // init star image views
        this.imgview_star1.setOnClickListener(this)
        imgViewStars.add(this.imgview_star1)

        this.imgview_star2.setOnClickListener(this)
        imgViewStars.add(this.imgview_star2)

        this.imgview_star3.setOnClickListener(this)
        imgViewStars.add(this.imgview_star3)

        this.imgview_star4.setOnClickListener(this)
        imgViewStars.add(this.imgview_star4)

        this.imgview_star5.setOnClickListener(this)
        imgViewStars.add(this.imgview_star5)

        val a = context.obtainStyledAttributes(attrs, R.styleable.ViewRateStar)
        val value = a.getFloat(R.styleable.ViewRateStar_starValue, 0.0F)
        starSelectable = a.getBoolean(R.styleable.ViewRateStar_starSelectable, false)
        updateStar(value.toDouble())

        a.recycle()
    }

    fun init() {
        View.inflate(context, R.layout.layout_star_rate, this)
    }

    fun updateStar(value: Double) {
        for (i in 0..4) {
            if (i < round(value)) {
                imgViewStars[i].setColorFilter(ContextCompat.getColor(context!!, R.color.colorStar))
            }
            else {
                imgViewStars[i].setColorFilter(ContextCompat.getColor(context!!, R.color.colorGrey))
            }
        }
    }

    override fun onClick(view: View?) {
        if (!starSelectable) {
            return
        }

        for (i in 0..4) {
            if (view == imgViewStars[i]) {
                updateStar((i + 1).toDouble())

                rateListener?.onSelectRate((i + 1).toDouble())
            }
        }
    }

    /**
     * interface for select rate
     */
    interface SelectRateListener {
        fun onSelectRate(rate: Double)
    }
}
