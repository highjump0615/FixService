package com.brainyapps.e2fix.activities.serviceman

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.View
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.activities.BaseActivity
import com.brainyapps.e2fix.activities.BaseDrawerActivity
import com.brainyapps.e2fix.activities.signin.LoginActivity
import com.brainyapps.e2fix.utils.Utils
import kotlinx.android.synthetic.main.app_bar_serviceman.*
import kotlinx.android.synthetic.main.layout_serviceman_drawer.*

/**
 * Created by Administrator on 2/14/18.
 */
open class BaseServicemanActivity : BaseDrawerActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initDrawer() {
        super.initDrawer()

        // add menu event
        this.layout_nav_available_job.setOnClickListener(this)
        this.layout_nav_bid_won.setOnClickListener(this)
        this.layout_logout.setOnClickListener(this)
        this.layout_nav_applied.setOnClickListener(this)
        this.layout_nav_setting.setOnClickListener(this)
        this.layout_nav_profile.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            // available jobs
            R.id.layout_nav_available_job -> {
                if (this is JobActivity) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
                else {
                    Utils.moveNextActivity(this, JobActivity::class.java, true)
                }
            }

            // bids win
            R.id.layout_nav_bid_won -> {
                if (this is BidActivity && this.type == BidActivity.TYPE_BID_WON) {
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
                if (this is BidActivity && this.type == BidActivity.TYPE_JOB_APPLIED) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
                else {
                    val intent = Intent(this, BidActivity::class.java)
                    intent.putExtra(BidActivity.KEY_TYPE, BidActivity.TYPE_JOB_APPLIED)
                    startActivity(intent)
                    finish()
                }
            }

            // log out
            R.id.layout_logout -> {
                Utils.moveNextActivity(this, LoginActivity::class.java, true)
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