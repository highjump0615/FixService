package com.brainyapps.e2fix.activities.signin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.activities.BaseActivity

class SignupStripeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_stripe)

        setNavbar("Payment Information", true)
    }
}
