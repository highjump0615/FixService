package com.brainyapps.ezfix.activities.serviceman

import android.content.Intent
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
import com.brainyapps.ezfix.R
import com.brainyapps.ezfix.activities.BaseDrawerActivity
import com.brainyapps.ezfix.activities.GeoLocationHelper
import com.brainyapps.ezfix.adapters.serviceman.JobItemAdapter
import com.brainyapps.ezfix.models.BaseModel
import com.brainyapps.ezfix.models.Bid
import com.brainyapps.ezfix.models.Job
import com.brainyapps.ezfix.models.User
import com.brainyapps.ezfix.utils.FirebaseManager
import com.brainyapps.ezfix.utils.PrefUtils
import com.brainyapps.ezfix.utils.Utils
import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation
import com.firebase.geofire.GeoQueryDataEventListener
import com.firebase.geofire.GeoQueryEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.app_bar_serviceman.view.*
import kotlinx.android.synthetic.main.layout_content_job.*

class JobAvailableActivity : BaseDrawerActivity(), SwipeRefreshLayout.OnRefreshListener {

    private val TAG = JobAvailableActivity::class.java.getSimpleName()

    private var aryJob = ArrayList<Job>()
    var adapter: JobItemAdapter? = null

    private var locationHelper: GeoLocationHelper? = null

    // job filter radius
    var mRadius = 0

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
            }

            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                getJobs(true, true)
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

        // init location
        this.locationHelper = GeoLocationHelper(this, "Browsing jobs needs location for distance filter")
    }

    override fun onRefresh() {
        getJobs(false, false)
    }

    override fun onResume() {
        super.onResume()

        // load radius from shared preference
        val nRadius = PrefUtils.instance!!.getInt(PrefUtils.PREF_FILTER_RADIUS, -1)
        if (mRadius != nRadius) {
            mRadius = nRadius

            // load data
            Handler().postDelayed({ getJobs(true, true) }, 500)
        }
        else {
            // update bid list
            fetchJobBidInfo()
        }
    }

    private fun clearJobList(bAnimation: Boolean) {
        if (bAnimation) {
            this@JobAvailableActivity.adapter!!.notifyItemRangeRemoved(0, aryJob.count())
        }
        aryJob.clear()
    }

    private fun addJobItem(data: DataSnapshot?) {
        data?.let {
            val job = it.getValue(Job::class.java)
            job!!.id = it.key

            if (job.category != this.spinner.selectedItemPosition) {
                return
            }

            aryJob.add(0, job)
        }
    }

    private fun updateList(bAnimation: Boolean) {
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

        // set bid info
        fetchJobBidInfo()
    }

    /**
     * get User data
     */
    private fun getJobs(bRefresh: Boolean, bAnimation: Boolean) {

        // exit if in processing,
        if (this.swiperefresh.isRefreshing) {
            return
        }

        if (bRefresh) {
            if (!this.swiperefresh.isRefreshing) {
                this.swiperefresh.isRefreshing = true
            }
        }

        //
        // query jobs based on distance
        //
        if (this.locationHelper!!.location != null && mRadius > 0) {
            // geofire
            val geoFire = GeoFire(FirebaseDatabase.getInstance().getReference(Job.TABLE_NAME))
            val geoQuery = geoFire.queryAtLocation(GeoLocation(this.locationHelper!!.location!!.latitude, this.locationHelper!!.location!!.longitude), mRadius * 1.60934)

            clearJobList(bAnimation)

            geoQuery.addGeoQueryDataEventListener(object: GeoQueryDataEventListener {
                override fun onGeoQueryReady() {
                    Log.d(TAG, "addGeoQueryEventListener:onGeoQueryReady")

                    stopRefresh()
                    updateList(bAnimation)
                }

                override fun onDataExited(dataSnapshot: DataSnapshot?) {
                    Log.d(TAG, "addGeoQueryEventListener:onDataExited $dataSnapshot")
                }

                override fun onDataChanged(dataSnapshot: DataSnapshot?, location: GeoLocation?) {
                    Log.d(TAG, "addGeoQueryEventListener:onDataChanged$dataSnapshot")
                }

                override fun onDataEntered(dataSnapshot: DataSnapshot?, location: GeoLocation?) {
                    Log.d(TAG, "addGeoQueryEventListener:onDataEntered$dataSnapshot")

                    addJobItem(dataSnapshot)
                }

                override fun onDataMoved(dataSnapshot: DataSnapshot?, location: GeoLocation?) {
                    Log.d(TAG, "addGeoQueryEventListener:onDataMoved$dataSnapshot")
                }

                override fun onGeoQueryError(error: DatabaseError?) {
                    Log.w(TAG, "addGeoQueryEventListener:failure", error?.toException())
                }
            })
        }
        //
        // normal job query
        //
        else {
            val database = FirebaseDatabase.getInstance().reference
            val query = database.child(Job.TABLE_NAME).orderByChild(BaseModel.FIELD_DATE)

            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    stopRefresh()

                    clearJobList(bAnimation)

                    for (jobItem in dataSnapshot.children) {
                        addJobItem(jobItem)
                    }

                    updateList(bAnimation)
                }

                override fun onCancelled(error: DatabaseError) {
                    stopRefresh()
                }
            })
        }
    }

    /**
     * filter jobs according to cateogry
     */
    private fun filterJobs(bAnimation: Boolean) {
    }

    /**
     * fetch user info for each job
     */
    private fun fetchJobUserInfo() {
        for (jobItem in aryJob) {
            User.readFromDatabase(jobItem.userId, object: User.FetchDatabaseListener {
                override fun onFetchedReviews() {
                }

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
            jobItem.fetchBidList(object: Job.FetchBidInfoListener {
                override fun onFetchedBid(success: Boolean) {
                    if (success) {
                        // update the list
                        this@JobAvailableActivity.adapter!!.notifyDataSetChanged()
                    }
                }
            })
        }
    }

    fun stopRefresh() {
        this.swiperefresh.isRefreshing = false
    }

    override fun onClick(view: View?) {
        super.onClick(view)

        when (view?.id) {
            // posted jobs
            R.id.imgview_right -> {
                Utils.moveNextActivity(this, ServicemanFilterActivity::class.java)
            }
        }
    }
}
