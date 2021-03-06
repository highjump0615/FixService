package com.brainyapps.ezfix.views.serviceman

import android.content.Context
import android.graphics.Color
import android.support.design.widget.AppBarLayout
import android.util.AttributeSet
import android.view.View
import android.support.v7.widget.Toolbar
import android.widget.ImageView
import com.brainyapps.ezfix.R
import com.brainyapps.ezfix.activities.BaseDrawerActivity
import kotlinx.android.synthetic.main.app_bar_serviceman.view.*

/**
 * Created by Administrator on 2/19/18.
 */
class ViewToolbar : Toolbar {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()

        val a = context.obtainStyledAttributes(attrs, R.styleable.ViewToolbar)
        val imgRight = a.getDrawable(R.styleable.ViewToolbar_rightImage)

        this.imgview_right?.setImageDrawable(imgRight)

        if (context is BaseDrawerActivity) {
            this.imgview_right?.setOnClickListener(context)
        }

        a.recycle()
    }

    fun init() {
        View.inflate(context, R.layout.app_bar_serviceman, this)

        setContentInsetsAbsolute(0, 0)
    }
}
