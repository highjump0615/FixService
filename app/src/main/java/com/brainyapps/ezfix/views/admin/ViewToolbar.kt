package com.brainyapps.ezfix.views.admin

import android.content.Context
import android.graphics.Color
import android.support.design.widget.AppBarLayout
import android.util.AttributeSet
import android.view.View
import android.support.v7.widget.Toolbar
import com.brainyapps.ezfix.R

/**
 * Created by Administrator on 2/19/18.
 */
class ViewToolbar : Toolbar {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    fun init() {
        View.inflate(context, R.layout.app_bar_admin, this)

        setContentInsetsAbsolute(0, 0)
    }
}
