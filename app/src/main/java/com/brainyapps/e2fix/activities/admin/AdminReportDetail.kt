package com.brainyapps.e2fix.activities.admin

import android.os.Bundle
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.activities.BaseActivity

class AdminReportDetail : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_report_detail)

        setNavbar("DAVID HASKINS", true)
    }
}
