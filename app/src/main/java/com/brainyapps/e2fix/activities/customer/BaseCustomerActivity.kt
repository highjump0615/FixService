package com.brainyapps.e2fix.activities.customer

import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.view.View
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.activities.BaseDrawerActivity
import com.brainyapps.e2fix.utils.Utils
import kotlinx.android.synthetic.main.layout_customer_drawer.*


/**
 * Created by Administrator on 2/14/18.
 */
open class BaseCustomerActivity : BaseDrawerActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initDrawer() {
        super.initDrawer()

        // add menu event
        this.layout_nav_posted_job.setOnClickListener(this)
        this.layout_nav_post.setOnClickListener(this)
        this.layout_nav_setting.setOnClickListener(this)
        this.layout_nav_profile.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            // posted jobs
            R.id.layout_nav_posted_job -> {
                if (this is JobActivity) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
                else {
                    Utils.moveNextActivity(this, JobActivity::class.java, true)
                }
            }

            // post job
            R.id.layout_nav_post -> {
                if (this is PostJobActivity) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
                else {
                    Utils.moveNextActivity(this, PostJobActivity::class.java, true)
                }
            }

            // setting
            R.id.layout_nav_setting -> {
                if (this is SettingsActivity) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
                else {
                    Utils.moveNextActivity(this, SettingsActivity::class.java, true)
                }
            }

            // profile
            R.id.layout_nav_profile -> {
                if (this is ProfileActivity) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
                else {
                    Utils.moveNextActivity(this, ProfileActivity::class.java, true)
                }
            }
        }
    }
}