package com.brainyapps.ezfix.activities.customer

import android.os.Bundle
import android.os.Handler
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.brainyapps.ezfix.R
import com.brainyapps.ezfix.activities.BaseDrawerActivity
import com.brainyapps.ezfix.adapters.serviceman.JobItemAdapter
import com.brainyapps.ezfix.models.BaseModel
import com.brainyapps.ezfix.models.Job
import com.brainyapps.ezfix.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.layout_content_customer_job.*
import java.util.*

class JobPostedActivity : BaseDrawerActivity(), SwipeRefreshLayout.OnRefreshListener {

    var adapter: JobItemAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_job)

        setNavbar()
        initDrawer(User.USER_TYPE_CUSTOMER)

        // init list
        val recyclerView = findViewById<RecyclerView>(R.id.list)

        val layoutManager = LinearLayoutManager(this)
        recyclerView.setLayoutManager(layoutManager)

        this.adapter = JobItemAdapter(this, User.currentUser!!.posts)
        recyclerView.setAdapter(this.adapter)
        recyclerView.setItemAnimator(DefaultItemAnimator())

        this.swiperefresh.setOnRefreshListener(this)

        if (User.currentUser!!.posts.isEmpty()) {
            // load data
            Handler().postDelayed({ getJobs(true, true) }, 500)
        }
    }

    override fun onResume() {
        super.onResume()

        // update bid info
        fetchJobBidInfo()
    }

    /**
     * get User data
     */
    fun getJobs(bRefresh: Boolean, bAnimation: Boolean) {

        if (bAnimation) {
            if (!this.swiperefresh.isRefreshing) {
                this.swiperefresh.isRefreshing = true
            }
        }

        val database = FirebaseDatabase.getInstance().reference
        val query = database.child(Job.TABLE_NAME).orderByChild(Job.FIELD_USERID).equalTo(User.currentUser!!.id)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                stopRefresh()

                var bEmpty = false
                // if empty, use animation for add
                if (User.currentUser!!.posts.isEmpty()) {
                    bEmpty = true
                }
                User.currentUser!!.posts.clear()

                if (!dataSnapshot.exists()) {
                    this@JobPostedActivity.text_empty_notice.visibility = View.VISIBLE
                }

                for (jobItem in dataSnapshot.children) {
                    val job = jobItem.getValue(Job::class.java)
                    job!!.id = jobItem.key
                    job.userPosted = User.currentUser

                    User.currentUser!!.posts.add(0, job)
                }

                // sort
                Collections.sort(User.currentUser!!.posts, Collections.reverseOrder())

                if (bEmpty) {
                    this@JobPostedActivity.adapter!!.notifyItemRangeInserted(0, User.currentUser!!.posts.count())
                }
                else {
                    this@JobPostedActivity.adapter!!.notifyDataSetChanged()
                }

                // set bid info
                fetchJobBidInfo()
            }

            override fun onCancelled(error: DatabaseError) {
                stopRefresh()
            }
        })
    }

    fun stopRefresh() {
        this.swiperefresh.isRefreshing = false
    }

    override fun onRefresh() {
        getJobs(true, false)
    }

    /**
     * fetch bid info for each job
     */
    private fun fetchJobBidInfo() {
        for (jobItem in User.currentUser!!.posts) {
            jobItem.fetchBidList(object: Job.FetchBidInfoListener {
                override fun onFetchedBid(success: Boolean) {
                    if (success) {
                        // update the list
                        this@JobPostedActivity.adapter!!.notifyDataSetChanged()
                    }
                }
            })
        }
    }
}
