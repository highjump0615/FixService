package com.brainyapps.ezfix.activities.admin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import com.brainyapps.ezfix.R
import com.brainyapps.ezfix.activities.BaseActivity

class AdminMainActivity : BaseActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_main)

        setNavbar("MAIN MENU", false)

        // user
        var layout = findViewById<RelativeLayout>(R.id.layout_user)
        layout.setOnClickListener(this)

        // reported user
        layout = findViewById<RelativeLayout>(R.id.layout_reported_user)
        layout.setOnClickListener(this)

        // settings
        layout = findViewById<RelativeLayout>(R.id.layout_setting)
        layout.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            // users
            R.id.layout_user -> {
                val intent = Intent(this@AdminMainActivity, AdminUserActivity::class.java)
                startActivity(intent)
            }
            // reported users
            R.id.layout_reported_user -> {
                val intent = Intent(this@AdminMainActivity, AdminReportUserActivity::class.java)
                startActivity(intent)
            }
            // settings
            R.id.layout_setting -> {
                val intent = Intent(this@AdminMainActivity, AdminSettingActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
