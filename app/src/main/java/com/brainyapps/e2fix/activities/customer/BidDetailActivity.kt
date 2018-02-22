package com.brainyapps.e2fix.activities.customer

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.activities.BaseActivity
import com.brainyapps.e2fix.adapters.customer.BidItemAdapter
import com.brainyapps.e2fix.models.Bid
import com.brainyapps.e2fix.utils.Utils

class BidDetailActivity : BaseActivity() {

    var aryBid = ArrayList<Bid>()
    var adapter: BidItemAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_bid_detail)

        setNavbar(null, true)

        // init data
        for (i in 0..10) {
            this.aryBid.add(Bid())
        }

        // init list
        val recyclerView = findViewById<RecyclerView>(R.id.list)

        val layoutManager = LinearLayoutManager(this)
        recyclerView.setLayoutManager(layoutManager)

        this.adapter = BidItemAdapter(this, this.aryBid)
        recyclerView.setAdapter(this.adapter)
        recyclerView.setItemAnimator(DefaultItemAnimator())
    }
}
