package com.brainyapps.e2fix.activities.serviceman

import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.activities.BaseDrawerActivity
import com.brainyapps.e2fix.adapters.serviceman.BidItemAdapter
import com.brainyapps.e2fix.models.Job
import com.brainyapps.e2fix.models.User

class BidActivity : BaseDrawerActivity() {

    companion object {
        val KEY_TYPE = "bidType"

        val TYPE_BID_WON = 1
        val TYPE_JOB_APPLIED = 2
    }

    var aryJob = ArrayList<Job>()
    var adapter: BidItemAdapter? = null

    var bidType = TYPE_BID_WON

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bid)

        setNavbar()
        initDrawer(User.USER_TYPE_SERVICEMAN)

        // get bidType from intent
        val bundle = intent.extras
        this.bidType = bundle.getInt(KEY_TYPE)

        // init data
        for (i in 0..14) {
            this.aryJob.add(Job())
        }

        // init list
        val recyclerView = findViewById<RecyclerView>(R.id.list)

        val layoutManager = LinearLayoutManager(this)
        recyclerView.setLayoutManager(layoutManager)

        this.adapter = BidItemAdapter(this, this.aryJob, this.bidType)
        recyclerView.setAdapter(this.adapter)
        recyclerView.setItemAnimator(DefaultItemAnimator())
    }
}
