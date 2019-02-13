package com.brainyapps.ezfix.activities.serviceman

import android.os.Bundle
import android.os.Handler
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.brainyapps.ezfix.R
import com.brainyapps.ezfix.activities.BaseDrawerActivity
import com.brainyapps.ezfix.adapters.serviceman.BidItemAdapter
import com.brainyapps.ezfix.models.Bid
import com.brainyapps.ezfix.models.Job
import com.brainyapps.ezfix.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.layout_content_bid.*
import java.util.*

class BidActivity : BaseDrawerActivity(), SwipeRefreshLayout.OnRefreshListener {

    companion object {
        val KEY_TYPE = "bidType"

        val TYPE_BID_WON = 1
        val TYPE_JOB_APPLIED = 2
    }

    var aryBid = ArrayList<Bid>()
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

        // set title
        if (this.bidType == TYPE_JOB_APPLIED) {
            this.text_job_title.text = "JOBS APPLIED"
            this.text_empty_notice.text = "There is no applied jobs yet"
        }

        // init list
        val layoutManager = LinearLayoutManager(this)
        this.list.setLayoutManager(layoutManager)

        this.adapter = BidItemAdapter(this, this.aryBid, this.bidType)
        this.list.setAdapter(this.adapter)
        this.list.setItemAnimator(DefaultItemAnimator())

        this.swiperefresh.setOnRefreshListener(this)

        // load data
        Handler().postDelayed({ getBids(false, true) }, 500)
    }

    /**
     * get Bid data
     */
    fun getBids(bRefresh: Boolean, bAnimation: Boolean) {
        if (bAnimation) {
            if (!this.swiperefresh.isRefreshing) {
                this.swiperefresh.isRefreshing = true
            }
        }

        val database = FirebaseDatabase.getInstance().reference
        val query = database.child(Bid.TABLE_NAME).orderByChild(Bid.FIELD_USERID).equalTo(User.currentUser!!.id)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if (bAnimation) {
                    this@BidActivity.adapter!!.notifyItemRangeRemoved(0, aryBid.count())
                }
                aryBid.clear()

                for (bidItem in dataSnapshot.children) {
                    val bid = bidItem.getValue(Bid::class.java)

                    // only won bids, if it is bid won job list
                    if (this@BidActivity.bidType == TYPE_BID_WON) {
                        if (!bid!!.isTaken) {
                            continue
                        }
                    }

                    aryBid.add(bid!!)
                }

                // show empty notice, if no data
                if (aryBid.isEmpty()) {
                    this@BidActivity.text_empty_notice.visibility = View.VISIBLE

                    stopRefresh()
                    return
                }

                fetchBidJobsInfo(bRefresh, bAnimation)
            }

            override fun onCancelled(error: DatabaseError) {
                stopRefresh()
            }
        })
    }

    fun fetchBidJobsInfo(bRefresh: Boolean, bAnimation: Boolean) {
        for (bidItem in aryBid) {
            Job.readFromDatabase(bidItem.jobId, object: Job.FetchDatabaseListener {
                override fun onFetchedJob(job: Job?, success: Boolean) {
                    bidItem.job = job

                    // check if all jobs are fetched
                    var nCount = 0
                    for (b in aryBid) {
                        if (b.job != null) {
                            nCount++
                        }
                    }
                    if (nCount >= aryBid.count()) {
                        if (!bRefresh) {
                            stopRefresh()
                        }

                        if (bAnimation) {
                            this@BidActivity.adapter!!.notifyItemRangeInserted(0, aryBid.count())
                        }
                        else {
                            this@BidActivity.adapter!!.notifyDataSetChanged()
                        }
                    }
                }
            })
        }
    }

    fun stopRefresh() {
        this.swiperefresh.isRefreshing = false
    }

    override fun onRefresh() {
        getBids(true, false)
    }
}
