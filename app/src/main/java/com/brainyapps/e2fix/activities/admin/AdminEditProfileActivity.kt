package com.brainyapps.e2fix.activities.admin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.activities.BaseActivity

class AdminEditProfileActivity : BaseActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_edit_profile)

        setNavbar("Edit Owner Profile", true)

        // user
        val button = findViewById<Button>(R.id.but_save)
        button.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            // save
            R.id.but_save -> {
                finish()
            }
        }
    }
}
