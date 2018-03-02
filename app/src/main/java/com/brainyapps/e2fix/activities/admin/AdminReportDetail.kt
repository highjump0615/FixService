package com.brainyapps.e2fix.activities.admin

import android.os.Bundle
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.activities.BaseActivity
import com.brainyapps.e2fix.activities.BaseUserDetailHelper

class AdminReportDetail : BaseActivity() {

    var helper: BaseUserDetailHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_report_detail)

        this.helper = BaseUserDetailHelper(this)

        setNavbar("DAVID HASKINS", true)
    }
}
