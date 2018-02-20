package com.brainyapps.e2fix.activities.signin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.activities.BaseActivity
import com.brainyapps.e2fix.activities.serviceman.JobActivity
import com.brainyapps.e2fix.utils.Utils
import kotlinx.android.synthetic.main.activity_signup_stripe.*

class SignupStripeActivity : BaseActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_stripe)

        setNavbar("Payment Information", true)

        this.but_done.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.but_done -> {
                Utils.moveNextActivity(this, JobActivity::class.java, true)
            }
        }
    }
}
