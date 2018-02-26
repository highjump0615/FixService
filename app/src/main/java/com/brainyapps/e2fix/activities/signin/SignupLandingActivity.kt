package com.brainyapps.e2fix.activities.signin

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.activities.BaseActivity
import com.brainyapps.e2fix.models.User
import kotlinx.android.synthetic.main.activity_signup_landing.*

class SignupLandingActivity : BaseActivity(), View.OnClickListener {
    companion object {
        val KEY_LOGIN_TYPE = "loginType"

        val LOGIN_TYPE_EMAIL = 0
        val LOGIN_TYPE_FACEBOOK = 1
        val LOGIN_TYPE_GOOGLE = 2
    }

    var loginType = LOGIN_TYPE_EMAIL

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.but_serviceman, R.id.but_customer -> {
                var intent = Intent(this@SignupLandingActivity, SignupEmailActivity::class.java)

                // if social login, go to sign up info page directly
                if (this.loginType > LOGIN_TYPE_EMAIL) {
                    intent = Intent(this@SignupLandingActivity, SignupInfoActivity::class.java)
                }

                val userType = if (view?.id == R.id.but_serviceman) {
                    User.USER_TYPE_SERVICEMAN
                }
                else {
                    User.USER_TYPE_CUSTOMER
                }
                intent.putExtra(SignupBaseActivity.KEY_USER_TYPE, userType)

                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_landing)

        setNavbar("Sign up", true)

        // init
        this.but_serviceman.setOnClickListener(this)
        this.but_customer.setOnClickListener(this)

        // get login type from intent
        val bundle = intent.extras
        if (bundle != null) {
            this.loginType = bundle.getInt(KEY_LOGIN_TYPE)
        }
    }
}
