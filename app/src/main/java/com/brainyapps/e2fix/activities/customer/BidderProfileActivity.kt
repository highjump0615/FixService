package com.brainyapps.e2fix.activities.customer

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.activities.BaseActivity
import com.brainyapps.e2fix.activities.UserDetailHelper
import com.brainyapps.e2fix.adapters.serviceman.JobItemAdapter
import com.brainyapps.e2fix.adapters.serviceman.ProfileAdapter
import com.brainyapps.e2fix.models.Job
import com.brainyapps.e2fix.models.Review
import com.brainyapps.e2fix.models.User
import kotlinx.android.synthetic.main.activity_bidder_profile.*

class BidderProfileActivity : BaseActivity(), SwipeRefreshLayout.OnRefreshListener {

    var adapter: ProfileAdapter? = null
    var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bidder_profile)

        setNavbar(null, true)

        // get user from intent
        val bundle = intent.extras
        this.user = bundle?.getParcelable<User>(UserDetailHelper.KEY_USER)

        // init list
        val recyclerView = findViewById<RecyclerView>(R.id.list)

        val layoutManager = LinearLayoutManager(this)
        recyclerView.setLayoutManager(layoutManager)

        this.adapter = ProfileAdapter(this, this.user!!)
        recyclerView.setAdapter(this.adapter)
        recyclerView.setItemAnimator(DefaultItemAnimator())

        this.swiperefresh.setOnRefreshListener(this)
    }

    override fun onRefresh() {
        getReviews(true, false)
    }

    private fun getReviews(bRefresh: Boolean, bAnimation: Boolean) {

        if (bAnimation) {
            if (!this.swiperefresh.isRefreshing) {
                this.swiperefresh.isRefreshing = true
            }
        }

        stopRefresh()
    }

    fun stopRefresh() {
        this.swiperefresh.isRefreshing = false
    }
}
