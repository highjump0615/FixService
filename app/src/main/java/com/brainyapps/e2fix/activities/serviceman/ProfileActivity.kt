package com.brainyapps.e2fix.activities.serviceman

import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.adapters.serviceman.JobItemAdapter
import com.brainyapps.e2fix.adapters.serviceman.ProfileAdapter
import com.brainyapps.e2fix.models.Job
import com.brainyapps.e2fix.models.Review

class ProfileActivity : BaseServicemanActivity() {

    var aryReview = ArrayList<Review>()
    var adapter: ProfileAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_serviceman_profile)

        setNavbar()
        initDrawer()

        // init data
        for (i in 0..15) {
            this.aryReview.add(Review())
        }

        // init list
        val recyclerView = findViewById<RecyclerView>(R.id.list)

        val layoutManager = LinearLayoutManager(this)
        recyclerView.setLayoutManager(layoutManager)

        this.adapter = ProfileAdapter(this, this.aryReview)
        recyclerView.setAdapter(this.adapter)
        recyclerView.setItemAnimator(DefaultItemAnimator())
    }
}
