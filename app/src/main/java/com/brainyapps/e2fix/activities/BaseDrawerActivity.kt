package com.brainyapps.e2fix.activities

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.view.View
import android.widget.RelativeLayout
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.activities.customer.JobPostedActivity
import com.brainyapps.e2fix.activities.customer.PostJobActivity
import com.brainyapps.e2fix.activities.customer.ProfileCustomerActivity
import com.brainyapps.e2fix.activities.serviceman.BidActivity
import com.brainyapps.e2fix.activities.serviceman.JobAvailableActivity
import com.brainyapps.e2fix.activities.serviceman.ProfileServicemanActivity
import com.brainyapps.e2fix.activities.signin.LoginActivity
import com.brainyapps.e2fix.models.User
import com.brainyapps.e2fix.utils.Utils
import kotlinx.android.synthetic.main.app_bar_serviceman.*

/**
 * Created by Administrator on 2/14/18.
 */
open class BaseDrawerActivity : BaseActivity(), View.OnClickListener {

    lateinit var drawerLayout: DrawerLayout
    var userType: Int = User.USER_TYPE_ADMIN

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    open fun initDrawer(type: Int) {
        drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)

        val toggle = ActionBarDrawerToggle(
                this, drawerLayout, this.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // init navigation menus
        this.userType = type
        var layout: RelativeLayout

        if (type == User.USER_TYPE_CUSTOMER) {
            // add menu event
            layout = findViewById<RelativeLayout>(R.id.layout_nav_posted_job)
            layout.setOnClickListener(this)

            layout = findViewById<RelativeLayout>(R.id.layout_nav_post)
            layout.setOnClickListener(this)
        }
        else if (type == User.USER_TYPE_SERVICEMAN) {
            layout = findViewById<RelativeLayout>(R.id.layout_nav_available_job)
            layout.setOnClickListener(this)

            layout = findViewById<RelativeLayout>(R.id.layout_nav_bid_won)
            layout.setOnClickListener(this)

            layout = findViewById<RelativeLayout>(R.id.layout_nav_applied)
            layout.setOnClickListener(this)
        }

        layout = findViewById<RelativeLayout>(R.id.layout_nav_setting)
        layout.setOnClickListener(this)

        layout = findViewById<RelativeLayout>(R.id.layout_nav_profile)
        layout.setOnClickListener(this)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            // posted jobs
            R.id.layout_nav_posted_job -> {
                if (this is JobPostedActivity) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
                else {
                    Utils.moveNextActivity(this, JobPostedActivity::class.java, true)
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
                if (this.userType == User.USER_TYPE_SERVICEMAN) {
                    if (this is ProfileServicemanActivity) {
                        drawerLayout.closeDrawer(GravityCompat.START)
                    }
                    else {
                        Utils.moveNextActivity(this, ProfileServicemanActivity::class.java, true)
                    }
                }
                else if (this.userType == User.USER_TYPE_CUSTOMER) {
                    if (this is ProfileCustomerActivity) {
                        drawerLayout.closeDrawer(GravityCompat.START)
                    }
                    else {
                        Utils.moveNextActivity(this, ProfileCustomerActivity::class.java, true)
                    }
                }
            }

            // available jobs
            R.id.layout_nav_available_job -> {
                if (this is JobAvailableActivity) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
                else {
                    Utils.moveNextActivity(this, JobAvailableActivity::class.java, true)
                }
            }

            // bids win
            R.id.layout_nav_bid_won -> {
                if (this is BidActivity && this.bidType == BidActivity.TYPE_BID_WON) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
                else {
                    val intent = Intent(this, BidActivity::class.java)
                    intent.putExtra(BidActivity.KEY_TYPE, BidActivity.TYPE_BID_WON)
                    startActivity(intent)
                    finish()
                }
            }

            // jobs applied
            R.id.layout_nav_applied -> {
                if (this is BidActivity && this.bidType == BidActivity.TYPE_JOB_APPLIED) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
                else {
                    val intent = Intent(this, BidActivity::class.java)
                    intent.putExtra(BidActivity.KEY_TYPE, BidActivity.TYPE_JOB_APPLIED)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}