package com.brainyapps.e2fix.activities

import android.os.Bundle
import android.view.View
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.activities.serviceman.EditProfileActivity
import com.brainyapps.e2fix.activities.signin.LoginActivity
import com.brainyapps.e2fix.models.User
import com.brainyapps.e2fix.utils.FirebaseManager
import com.brainyapps.e2fix.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.layout_content_serviceman_setting.*

open class SettingsActivity : BaseDrawerActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (User.currentUser!!.type == User.USER_TYPE_SERVICEMAN) {
            setContentView(R.layout.activity_serviceman_setting)
            setNavbar()
            initDrawer(User.USER_TYPE_SERVICEMAN)
        }
        else {
            setContentView(R.layout.activity_customer_setting)
            setNavbar()
            initDrawer(User.USER_TYPE_CUSTOMER)

            this.layout_payment.visibility = View.GONE
        }

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
            // log out
            R.id.layout_logout -> {
                FirebaseManager.mAuth.signOut()
                Utils.moveNextActivity(this, LoginActivity::class.java, true, true)
            }
        }
    }
}
