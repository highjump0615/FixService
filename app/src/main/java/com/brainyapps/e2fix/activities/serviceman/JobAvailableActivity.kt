package com.brainyapps.e2fix.activities.serviceman

import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ArrayAdapter
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.activities.BaseDrawerActivity
import com.brainyapps.e2fix.adapters.serviceman.JobItemAdapter
import com.brainyapps.e2fix.models.Job
import com.brainyapps.e2fix.models.User
import kotlinx.android.synthetic.main.layout_content_job.*

class JobAvailableActivity : BaseDrawerActivity() {

    var aryJob = ArrayList<Job>()
    var adapter: JobItemAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job)

        setNavbar()
        initDrawer(User.USER_TYPE_SERVICEMAN)

        // init spinner
        val adapter = ArrayAdapter.createFromResource(this, R.array.jobtypes_array, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        this.spinner.adapter = adapter

        // init data
        for (i in 0..15) {
            this.aryJob.add(Job())
        }

        // init list
        val recyclerView = findViewById<RecyclerView>(R.id.list)

        val layoutManager = LinearLayoutManager(this)
        recyclerView.setLayoutManager(layoutManager)

        this.adapter = JobItemAdapter(this, this.aryJob)
        recyclerView.setAdapter(this.adapter)
        recyclerView.setItemAnimator(DefaultItemAnimator())
    }
}
