package com.brainyapps.e2fix.activities.serviceman

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.activities.BaseActivity
import com.brainyapps.e2fix.utils.Utils
import kotlinx.android.synthetic.main.activity_bid_submit.*

class BidSubmitActivity : BaseActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bid_submit)

        setNavbar(null, true)

        this.but_submit.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            // submit button
            R.id.but_submit -> {
                finish()
            }
        }
    }
}
