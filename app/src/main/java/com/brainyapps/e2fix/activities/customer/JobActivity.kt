package com.brainyapps.e2fix.activities.customer

import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ArrayAdapter
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.adapters.serviceman.JobItemAdapter
import com.brainyapps.e2fix.models.Job
import com.brainyapps.e2fix.models.User
import kotlinx.android.synthetic.main.layout_content_job.*

class JobActivity : BaseCustomerActivity() {

    var aryJob = ArrayList<Job>()
    var adapter: JobItemAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_job)

        setNavbar()
        initDrawer()

        // init data
        for (i in 0..10) {
            val j = Job()
            j.userPosted = User()

            this.aryJob.add(j)
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
