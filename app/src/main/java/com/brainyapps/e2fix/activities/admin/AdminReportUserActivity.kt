package com.brainyapps.e2fix.activities.admin

import android.os.Bundle
import android.os.Handler
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.activities.BaseActivity
import com.brainyapps.e2fix.adapters.admin.UserItemAdapter
import com.brainyapps.e2fix.models.User
import kotlinx.android.synthetic.main.activity_admin_report_user.*
import java.util.ArrayList

class AdminReportUserActivity : BaseActivity(), SwipeRefreshLayout.OnRefreshListener {

    var aryUser = ArrayList<User>()

    var adapter: UserItemAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_report_user)

        setNavbar("REPORTED USERS", true)

        this.text_empty_notice.visibility = View.VISIBLE

        // init list
        val recyclerView = findViewById<RecyclerView>(R.id.list)

        val layoutManager = LinearLayoutManager(this)
        recyclerView.setLayoutManager(layoutManager)

        this.adapter = UserItemAdapter(this, this.aryUser)
        recyclerView.setAdapter(this.adapter)
        recyclerView.setItemAnimator(DefaultItemAnimator())

        this.swiperefresh.setOnRefreshListener(this)
    }

    override fun onRefresh() {
        Handler().postDelayed({ stopRefresh() }, 1500)
    }

    fun stopRefresh() {
        this.swiperefresh.isRefreshing = false
    }
}
