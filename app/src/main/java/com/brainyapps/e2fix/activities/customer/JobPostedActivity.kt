package com.brainyapps.e2fix.activities.customer

import android.os.Bundle
import android.os.Handler
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.activities.BaseDrawerActivity
import com.brainyapps.e2fix.adapters.serviceman.JobItemAdapter
import com.brainyapps.e2fix.models.BaseModel
import com.brainyapps.e2fix.models.Job
import com.brainyapps.e2fix.models.User
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

    /**
     * get User data
     */
    fun getJobs(bRefresh: Boolean, bAnimation: Boolean) {

        if (bAnimation) {
            if (!this.swiperefresh.isRefreshing) {
                this.swiperefresh.isRefreshing = true
            }
        }

        val thread = object : Thread() {
            override fun run() {
                try {
                }
                catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }

        val database = FirebaseDatabase.getInstance().reference
        val query = database.child(Job.TABLE_NAME).orderByChild(Job.FIELD_USERID).equalTo(User.currentUser!!.id)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                stopRefresh()

                this@JobPostedActivity.adapter!!.notifyItemRangeRemoved(0, User.currentUser!!.posts.count())
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

                this@JobPostedActivity.adapter!!.notifyItemRangeInserted(0, User.currentUser!!.posts.count())
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
}
