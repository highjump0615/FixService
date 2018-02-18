package com.brainyapps.e2fix.activities.admin

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.activities.BaseActivity

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
            }
            // settings
            R.id.layout_setting -> {
            }
        }
    }
}
