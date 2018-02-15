package com.brainyapps.e2fix.activities.signin

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.view.View
import android.widget.RelativeLayout
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.activities.BaseActivity

/**
 * Created by Administrator on 2/15/18.
 */
open class SignupBaseActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(view: View?) {
    }

    lateinit var mbutNext: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun initView(layoutResID: Int) {
        setContentView(layoutResID)

        setNavbar("Sign up", true)

        mbutNext = findViewById<View>(R.id.but_next) as RelativeLayout
        mbutNext.setOnClickListener(this)
    }
}