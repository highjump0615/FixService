package com.brainyapps.ezfix.activities.signin

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import com.brainyapps.ezfix.R
import com.brainyapps.ezfix.activities.BaseActivity
import com.brainyapps.ezfix.models.User

/**
 * Created by Administrator on 2/15/18.
 */
open class SignupBaseActivity : BaseActivity(), View.OnClickListener {

    companion object {
        val KEY_USER_TYPE = "bidType"
        val KEY_EMAIL = "email"
        val KEY_PASSWORD = "password"
    }

    var userType = User.USER_TYPE_ADMIN
    var email: String? = null
    var password: String? = null

    override fun onClick(view: View?) {
    }

    lateinit var mbutNext: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // get user type from intent
        val bundle = intent.extras
        if (bundle != null) {
            this.userType = bundle.getInt(SignupBaseActivity.KEY_USER_TYPE)
            this.email = bundle.getString(SignupBaseActivity.KEY_EMAIL)
            this.password = bundle.getString(SignupBaseActivity.KEY_PASSWORD)
        }
    }

    fun initView(layoutResID: Int, title: String = "Sign up") {
        setContentView(layoutResID)

        setNavbar(title, true)

        mbutNext = findViewById<View>(R.id.but_next) as RelativeLayout
        mbutNext.setOnClickListener(this)

        // disable next button
        enableNextButton(false)
    }

    fun enableNextButton(enable: Boolean) {
        enableButton(mbutNext, enable)
    }
}