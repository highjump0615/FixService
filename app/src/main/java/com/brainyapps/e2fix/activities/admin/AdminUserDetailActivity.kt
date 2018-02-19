package com.brainyapps.e2fix.activities.admin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.activities.BaseActivity

class AdminUserDetailActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_user_detail)

        setNavbar("USER INFO", true)
    }
}
