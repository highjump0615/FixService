package com.brainyapps.ezfix.activities.admin

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import com.brainyapps.ezfix.R
import com.brainyapps.ezfix.activities.BaseActivity
import com.brainyapps.ezfix.activities.signin.LoginActivity
import com.brainyapps.ezfix.utils.Utils

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
                signOutClear()
                Utils.moveNextActivity(this@AdminSettingActivity, LoginActivity::class.java, true, true)
            }
        }
    }
}
