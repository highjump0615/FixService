package com.brainyapps.e2fix.activities.serviceman

import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ArrayAdapter
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.activities.BaseActivity
import com.brainyapps.e2fix.adapters.serviceman.BidItemAdapter
import com.brainyapps.e2fix.models.Job
import kotlinx.android.synthetic.main.activity_job.*
import kotlinx.android.synthetic.main.app_bar_serviceman.*
import kotlinx.android.synthetic.main.layout_content_job.*

class BidActivity : BaseServicemanActivity() {

    var aryJob = ArrayList<Job>()
    var adapter: BidItemAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bid)

        setNavbar()
        initDrawer()

        // init data
        for (i in 0..14) {
            this.aryJob.add(Job())
        }

        // init list
        val recyclerView = findViewById<RecyclerView>(R.id.list)

        val layoutManager = LinearLayoutManager(this)
        recyclerView.setLayoutManager(layoutManager)

        this.adapter = BidItemAdapter(this, this.aryJob)
        recyclerView.setAdapter(this.adapter)
        recyclerView.setItemAnimator(DefaultItemAnimator())
    }
}
