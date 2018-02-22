package com.brainyapps.e2fix.activities.customer

import android.os.Bundle
import android.view.View
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.activities.serviceman.EditProfileActivity
import com.brainyapps.e2fix.utils.Utils
import kotlinx.android.synthetic.main.layout_content_serviceman_setting.*

class SettingsActivity : BaseCustomerActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_setting)

        setNavbar()
        initDrawer()

        this.layout_edit_profile.setOnClickListener(this)
        this.layout_logout.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        super.onClick(view)

        when (view?.id) {
            // edit profile
            R.id.layout_edit_profile -> {
                Utils.moveNextActivity(this, EditProfileActivity::class.java)
            }
        }
    }
}
