package com.brainyapps.e2fix.activities.signin

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.activities.BaseActivity
import com.brainyapps.e2fix.models.User

class SignupLandingActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.but_serviceman, R.id.but_customer -> {
                val intent = Intent(this@SignupLandingActivity, SignupEmailActivity::class.java)

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
        var button = findViewById<View>(R.id.but_serviceman)
        button.setOnClickListener(this)
        button = findViewById<View>(R.id.but_customer)
        button.setOnClickListener(this)
    }
}
