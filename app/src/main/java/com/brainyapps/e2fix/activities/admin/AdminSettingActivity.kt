package com.brainyapps.e2fix.activities.admin

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.activities.BaseActivity
import com.brainyapps.e2fix.activities.signin.LoginActivity
import com.brainyapps.e2fix.utils.Utils

class AdminSettingActivity : BaseActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_setting)

        setNavbar("SETTINGS", true)

        // edit profile
        var layout = findViewById<RelativeLayout>(R.id.layout_edit_profile)
        layout.setOnClickListener(this)

        // log out
        layout = findViewById<RelativeLayout>(R.id.layout_logout)
        layout.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            // edit profile
            R.id.layout_edit_profile -> {
                Utils.moveNextActivity(this@AdminSettingActivity, AdminEditProfileActivity::class.java)
            }
            // logout
            R.id.layout_logout-> {
                Utils.moveNextActivity(this@AdminSettingActivity, LoginActivity::class.java, true)
            }
        }
    }
}
