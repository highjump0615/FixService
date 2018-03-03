package com.brainyapps.e2fix.activities.serviceman

import android.os.Bundle
import android.os.Handler
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.activities.BaseDrawerActivity
import com.brainyapps.e2fix.adapters.serviceman.JobItemAdapter
import com.brainyapps.e2fix.models.BaseModel
import com.brainyapps.e2fix.models.Bid
import com.brainyapps.e2fix.models.Job
import com.brainyapps.e2fix.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.layout_content_job.*

class JobAvailableActivity : BaseDrawerActivity(), SwipeRefreshLayout.OnRefreshListener {

    var aryJob = ArrayList<Job>()
    var adapter: JobItemAdapter? = null

    var dataFetched: DataSnapshot? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job)

        setNavbar()
        initDrawer(User.USER_TYPE_SERVICEMAN)

        // init spinner
        val adapterSpinner = ArrayAdapter.createFromResource(this, R.array.jobtypes_array, android.R.layout.simple_spinner_item)
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        this.spinner.adapter = adapterSpinner
        this.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parentView: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                filterJobs(true)
            }

        }

        // init list
        val recyclerView = findViewById<RecyclerView>(R.id.list)

        val layoutManager = LinearLayoutManager(this)
        recyclerView.setLayoutManager(layoutManager)

        this.adapter = JobItemAdapter(this, this.aryJob)
        recyclerView.setAdapter(this.adapter)
        recyclerView.setItemAnimator(DefaultItemAnimator())

        this.swiperefresh.setOnRefreshListener(this)

        // load data
        Handler().postDelayed({ getJobs(true, true) }, 500)
    }

    override fun onRefresh() {
        getJobs(true, false)
    }

    override fun onResume() {
        super.onResume()

        // update bid list
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
        val query = database.child(Job.TABLE_NAME).orderByChild(BaseModel.FIELD_DATE)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                stopRefresh()

                this@JobAvailableActivity.dataFetched = dataSnapshot

                filterJobs(bAnimation)
            }

            override fun onCancelled(error: DatabaseError) {
                stopRefresh()
            }
        })
    }

    /**
     * filter jobs according to cateogry
     */
    private fun filterJobs(bAnimation: Boolean) {

        // not fetched data yet, return
        if (this.dataFetched == null) {
            return
        }

        if (bAnimation) {
            this.adapter!!.notifyItemRangeRemoved(0, aryJob.count())
        }
        aryJob.clear()


        for (jobItem in this.dataFetched!!.children) {
            val job = jobItem.getValue(Job::class.java)
            job!!.id = jobItem.key

            if (job.category != this.spinner.selectedItemPosition) {
                continue
            }

            aryJob.add(0, job)
        }

        // show empty notice if list is empty
        if (aryJob.isEmpty()) {
            this.text_empty_notice.visibility = View.VISIBLE
        }
        else {
            this.text_empty_notice.visibility = View.INVISIBLE
        }

        if (bAnimation) {
            this@JobAvailableActivity.adapter!!.notifyItemRangeInserted(0, aryJob.count())
        }
        else {
            this@JobAvailableActivity.adapter!!.notifyDataSetChanged()
        }

        // set user info
        fetchJobUserInfo()

        // set bid info
        fetchJobBidInfo()
    }

    /**
     * fetch user info for each job
     */
    private fun fetchJobUserInfo() {
        for (jobItem in aryJob) {
            User.readFromDatabase(jobItem.userId, object: User.FetchDatabaseListener {
                override fun onFetchedUser(user: User?, success: Boolean) {
                    jobItem.userPosted = user

                    // update the list
                    this@JobAvailableActivity.adapter!!.notifyDataSetChanged()
                }
            })
        }
    }

    /**
     * fetch bid info for each job
     */
    private fun fetchJobBidInfo() {
        for (jobItem in aryJob) {

            val database = FirebaseDatabase.getInstance().reference
            val query = database.child(Bid.TABLE_NAME).orderByChild(Bid.FIELD_JOBID).equalTo(jobItem.id)

            // Read from the database
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    jobItem.bidArray.clear()

                    for (bidItem in dataSnapshot.children) {
                        val bid = bidItem.getValue(Bid::class.java)
                        bid!!.id = bidItem.key

                        jobItem.bidArray.add(bid)
                    }

                    // update the list
                    this@JobAvailableActivity.adapter!!.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                    Log.w(User.TAG, "failed to read from database.", error.toException())
                }
            })

            User.readFromDatabase(jobItem.userId, object: User.FetchDatabaseListener {
                override fun onFetchedUser(user: User?, success: Boolean) {
                    jobItem.userPosted = user

                    // update the list
                    this@JobAvailableActivity.adapter!!.notifyDataSetChanged()
                }
            })
        }
    }

    fun stopRefresh() {
        this.swiperefresh.isRefreshing = false
    }
}
