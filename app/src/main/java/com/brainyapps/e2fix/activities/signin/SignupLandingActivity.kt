package com.brainyapps.e2fix.activities.signin

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.TextView
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.activities.BaseActivity

class SignupLandingActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.but_serviceman, R.id.but_customer -> {
                val signupIntent = Intent(this@SignupLandingActivity, SignupEmailActivity::class.java)
                startActivity(signupIntent)
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
