package com.brainyapps.e2fix.activities.serviceman

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.activities.BaseDrawerActivity
import com.brainyapps.e2fix.adapters.serviceman.JobItemAdapter
import com.brainyapps.e2fix.adapters.serviceman.ProfileAdapter
import com.brainyapps.e2fix.models.Job
import com.brainyapps.e2fix.models.Review
import com.brainyapps.e2fix.models.User
import kotlinx.android.synthetic.main.layout_content_serviceman_profile.*

class ProfileServicemanActivity : BaseDrawerActivity(), SwipeRefreshLayout.OnRefreshListener {

    var adapter: ProfileAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_serviceman_profile)

        setNavbar()
        initDrawer(User.USER_TYPE_SERVICEMAN)

        // init list
        val recyclerView = findViewById<RecyclerView>(R.id.list)

        val layoutManager = LinearLayoutManager(this)
        recyclerView.setLayoutManager(layoutManager)

        this.adapter = ProfileAdapter(this, User.currentUser!!)
        recyclerView.setAdapter(this.adapter)
        recyclerView.setItemAnimator(DefaultItemAnimator())

        this.swiperefresh.setOnRefreshListener(this)

        getReviews(false, false)
    }

    override fun onRefresh() {
        getReviews(true, false)
    }

    override fun onStop() {
        super.onStop()

        User.currentUser!!.reviews.clear()
    }

    private fun getReviews(bRefresh: Boolean, bAnimation: Boolean) {

        if (bAnimation) {
            if (!this.swiperefresh.isRefreshing) {
                this.swiperefresh.isRefreshing = true
            }
        }

        val user = User.currentUser
        user?.fetchReviews(object: User.FetchDatabaseListener {
            override fun onFetchedUser(user: User?, success: Boolean) {
            }

            override fun onFetchedReviews() {
                if (bRefresh) {
                    this@ProfileServicemanActivity.adapter?.notifyDataSetChanged()
                }
                else {
                    this@ProfileServicemanActivity.adapter?.notifyItemChanged(0)
                    this@ProfileServicemanActivity.adapter?.notifyItemRangeInserted(1, user.reviews.count())
                }

                stopRefresh()
            }
        })
    }

    fun stopRefresh() {
        this.swiperefresh.isRefreshing = false
    }
}
