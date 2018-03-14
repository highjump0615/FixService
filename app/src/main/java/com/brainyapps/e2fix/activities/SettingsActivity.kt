package com.brainyapps.e2fix.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.activities.serviceman.EditProfileActivity
import com.brainyapps.e2fix.activities.serviceman.TermActivity
import com.brainyapps.e2fix.activities.signin.LoginActivity
import com.brainyapps.e2fix.models.User
import com.brainyapps.e2fix.utils.FirebaseManager
import com.brainyapps.e2fix.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import com.kobakei.ratethisapp.RateThisApp
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
        }

        this.layout_edit_profile.setOnClickListener(this)
        this.layout_payment.setOnClickListener(this)
        this.layout_privacy.setOnClickListener(this)
        this.layout_term.setOnClickListener(this)
        this.layout_report.setOnClickListener(this)
        this.layout_rate.setOnClickListener(this)
        this.layout_logout.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        super.onClick(view)

        when (view?.id) {
            // edit profile
            R.id.layout_edit_profile -> {
                Utils.moveNextActivity(this, EditProfileActivity::class.java)
            }

            // report a problem
            R.id.layout_report -> {
                val emailintent = Intent(Intent.ACTION_SENDTO)
                val uriText = "mailto:Info.Pwas@gmail.com" +
                        "?subject=" + Uri.encode("E2Fix Report a Problem") +
                        "&body=" + Uri.encode("E2Fix is not working properly for me.\n" +
                        "Here is a brief description of what's going on:\n\n" + "Sent from my Android")

                emailintent.data = Uri.parse(uriText)
                startActivity(emailintent)
            }

            // log out
            R.id.layout_logout -> {
                signOutClear()
                Utils.moveNextActivity(this, LoginActivity::class.java, true, true)
            }

            // privacy policy
            R.id.layout_privacy -> {
                val intent = Intent(this, TermActivity::class.java)
                intent.putExtra(TermActivity.KEY_TERM_TYPE, TermActivity.TERM_POLICY)
                startActivity(intent)
            }

            // terms
            R.id.layout_term -> {
                val intent = Intent(this, TermActivity::class.java)
                intent.putExtra(TermActivity.KEY_TERM_TYPE, TermActivity.TERM_SERVICE)
                startActivity(intent)
            }

            // rate app
            R.id.layout_rate -> {
                RateThisApp.showRateDialog(this)
            }
        }
    }
}
