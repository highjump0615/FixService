package com.brainyapps.e2fix.activities.serviceman

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.activities.BaseActivity
import com.brainyapps.e2fix.utils.Utils
import kotlinx.android.synthetic.main.activity_bid_submit.*

class BidDetailApplyActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bid_detail_apply)

        setNavbar(null, true)
    }
}
