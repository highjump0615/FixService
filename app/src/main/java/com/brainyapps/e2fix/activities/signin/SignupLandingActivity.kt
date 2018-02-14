package com.brainyapps.e2fix.activities.signin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.TextView
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.activities.BaseActivity

class SignupLandingActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_landing)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        setNavbar("Sign up", true)
    }
}
