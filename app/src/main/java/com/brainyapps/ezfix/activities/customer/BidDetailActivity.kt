package com.brainyapps.ezfix.activities.customer

import android.os.Bundle
import android.os.Handler
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.brainyapps.ezfix.R
import com.brainyapps.ezfix.activities.BaseActivity
import com.brainyapps.ezfix.activities.GeoLocationHelper
import com.brainyapps.ezfix.activities.JobDetailHelper
import com.brainyapps.ezfix.adapters.customer.BidItemAdapter
import com.brainyapps.ezfix.models.Bid
import com.brainyapps.ezfix.models.Job
import com.brainyapps.ezfix.models.User
import com.brainyapps.ezfix.utils.CommonObjects
import kotlinx.android.synthetic.main.activity_customer_bid_detail.*

class BidDetailActivity : BaseActivity(), SwipeRefreshLayout.OnRefreshListener {

    var adapter: BidItemAdapter? = null

    var locationHelper: GeoLocationHelper? = null

    var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_bid_detail)

        setNavbar(null, true)

        // get job from common objects
        this.job = CommonObjects.selectedJob!!

        // init list
        val recyclerView = findViewById<RecyclerView>(R.id.list)

        val layoutManager = LinearLayoutManager(this)
        recyclerView.setLayoutManager(layoutManager)

        this.adapter = BidItemAdapter(this)
        recyclerView.setAdapter(this.adapter)
        recyclerView.setItemAnimator(DefaultItemAnimator())

        this.swiperefresh.setOnRefreshListener(this)

        // check bid status
        showEmptyNotice()

        Handler().postDelayed({ fetchBidUserInfo(true) }, 500)

        // init location
        this.locationHelper = GeoLocationHelper(this, "Location is needed for showing distance from bidders")
    }

    override fun onResume() {
        super.onResume()

        // refresh list
        this.adapter!!.notifyDataSetChanged()
    }

    fun fetchBidUserInfo(animated: Boolean) {
        for (bidItem in this.job!!.bidArray) {
            if (animated) {
                if (!this.swiperefresh.isRefreshing) {
                    this.swiperefresh.isRefreshing = true
                }
            }

            User.readFromDatabase(bidItem.userId, object: User.FetchDatabaseListener {
                override fun onFetchedReviews() {
                }

                override fun onFetchedUser(user: User?, success: Boolean) {
                    bidItem.user = user

                    updateBidList()
                }
            })
        }
    }

    /**
     * update bid list when all the bid users are fetched
     */
    fun updateBidList() {
        var bFetchedAll = true

        for (bidItem in this.job!!.bidArray) {
            if (bidItem.user == null) {
                bFetchedAll = false
                break
            }
        }

        if (bFetchedAll) {
            this.adapter!!.notifyDataSetChanged()

            stopRefresh()
        }
    }

    private fun showEmptyNotice() {
        if (this.job!!.bidArray.isEmpty()) {
            this.text_empty_notice.visibility = View.VISIBLE
        }
        else {
            this.text_empty_notice.visibility = View.GONE
        }
    }

    override fun onRefresh() {
        // reload bid status
        this.job!!.fetchBidList(object: Job.FetchBidInfoListener {
            override fun onFetchedBid(success: Boolean) {
                if (success) {
                    showEmptyNotice()
                    fetchBidUserInfo(false)
                }
            }
        })
    }

    fun stopRefresh() {
        this.swiperefresh.isRefreshing = false
    }
}
