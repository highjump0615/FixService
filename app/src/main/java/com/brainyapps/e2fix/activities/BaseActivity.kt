package com.brainyapps.e2fix.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.activities.admin.AdminMainActivity
import com.brainyapps.e2fix.activities.customer.JobPostedActivity
import com.brainyapps.e2fix.activities.serviceman.JobAvailableActivity
import com.brainyapps.e2fix.models.User
import com.brainyapps.e2fix.utils.FirebaseManager
import com.brainyapps.e2fix.utils.Utils

/**
 * Created by Administrator on 2/14/18.
 */
open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()

        // close progress dialog
        Utils.closeProgressDialog()
    }

    fun setNavbar(title: String? = null, withBackButton: Boolean = false) {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        // set title
        if (!TextUtils.isEmpty(title)) {
            val textTitle = findViewById<View>(R.id.text_title) as TextView
            textTitle.setText(title)
        }

        // back button
        if (withBackButton) {
            // back icon
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)

            // back icon event
            toolbar.setNavigationOnClickListener { onBackPressed() }
        }
    }

    /**
     * go to main page according to user type
     */
    fun goToMain() {
        when (User.currentUser!!.type) {
            User.USER_TYPE_ADMIN -> {
                Utils.moveNextActivity(this, AdminMainActivity::class.java, true, true)
            }
            User.USER_TYPE_SERVICEMAN -> {
                Utils.moveNextActivity(this, JobAvailableActivity::class.java, true, true)
            }
            User.USER_TYPE_CUSTOMER -> {
                Utils.moveNextActivity(this, JobPostedActivity::class.java, true, true)
            }
        }
    }

    /**
     * sign out & clear data
     */
    fun signOutClear() {
        FirebaseManager.mAuth.signOut()
        User.currentUser = null
    }
}