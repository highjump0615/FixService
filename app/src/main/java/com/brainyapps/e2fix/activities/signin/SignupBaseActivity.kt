package com.brainyapps.e2fix.activities.signin

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.activities.BaseActivity
import com.brainyapps.e2fix.models.User

/**
 * Created by Administrator on 2/15/18.
 */
open class SignupBaseActivity : BaseActivity(), View.OnClickListener {

    companion object {
        val KEY_USER_TYPE = "bidType"
    }

    var userType = User.USER_TYPE_ADMIN

    override fun onClick(view: View?) {
    }

    lateinit var mbutNext: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // get user type from intent
        val bundle = intent.extras
        this.userType = bundle.getInt(SignupBaseActivity.KEY_USER_TYPE)
    }

    fun initView(layoutResID: Int) {
        setContentView(layoutResID)

        setNavbar("Sign up", true)

        mbutNext = findViewById<View>(R.id.but_next) as RelativeLayout
        mbutNext.setOnClickListener(this)
    }
}