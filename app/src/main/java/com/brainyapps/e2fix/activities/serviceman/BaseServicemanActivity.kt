package com.brainyapps.e2fix.activities.serviceman

import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.activities.BaseActivity
import com.brainyapps.e2fix.activities.signin.LoginActivity
import com.brainyapps.e2fix.utils.Utils
import kotlinx.android.synthetic.main.activity_job.*
import kotlinx.android.synthetic.main.app_bar_serviceman.*
import kotlinx.android.synthetic.main.layout_serviceman_drawer.*

/**
 * Created by Administrator on 2/14/18.
 */
open class BaseServicemanActivity : BaseActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun initDrawer() {
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, this.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        // add menu event
        this.layout_nav_available_job.setOnClickListener(this)
        this.layout_nav_bid_won.setOnClickListener(this)
        this.layout_logout.setOnClickListener(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            // available jobs
            R.id.layout_nav_available_job -> {
                if (this is JobActivity) {
                    drawer_layout.closeDrawer(GravityCompat.START)
                }
                else {
                    Utils.moveNextActivity(this, JobActivity::class.java, true)
                }
            }

            // bids win
            R.id.layout_nav_bid_won -> {
                if (this is BidActivity) {
                    drawer_layout.closeDrawer(GravityCompat.START)
                }
                else {
                    Utils.moveNextActivity(this, BidActivity::class.java, true)
                }
            }

            // log out
            R.id.layout_logout -> {
                Utils.moveNextActivity(this, LoginActivity::class.java, true)
            }
        }
    }
}