package com.brainyapps.e2fix.activities.admin

import android.os.Bundle
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.activities.BaseActivity
import com.brainyapps.e2fix.activities.UserDetailHelper

class AdminReportDetail : BaseActivity() {

    var helper: UserDetailHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_report_detail)

        setNavbar("DAVID HASKINS", true)
    }
}
