package com.brainyapps.e2fix.activities.admin

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.activities.BaseActivity
import com.brainyapps.e2fix.adapters.admin.ReportItemAdapter
import com.brainyapps.e2fix.models.BaseModel
import com.brainyapps.e2fix.models.Report
import com.brainyapps.e2fix.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_admin_report_user.*
import java.util.ArrayList

class AdminReportUserActivity : BaseActivity(), SwipeRefreshLayout.OnRefreshListener {

    var aryReport = ArrayList<Report>()

    var adapter: ReportItemAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_report_user)

        setNavbar("REPORTED USERS", true)

        // init list
        val recyclerView = findViewById<RecyclerView>(R.id.list)

        val layoutManager = LinearLayoutManager(this)
        recyclerView.setLayoutManager(layoutManager)

        this.adapter = ReportItemAdapter(this, this.aryReport)
        recyclerView.setAdapter(this.adapter)
        recyclerView.setItemAnimator(DefaultItemAnimator())

        this.swiperefresh.setOnRefreshListener(this)

        // load data
        Handler().postDelayed({ getReports(true, true) }, 500)
    }

    override fun onRefresh() {
        getReports(true, false)
    }

    fun stopRefresh() {
        this.swiperefresh.isRefreshing = false
    }

    /**
     * get User data
     */
    private fun getReports(bRefresh: Boolean, bAnimation: Boolean) {

        if (bAnimation) {
            if (!this.swiperefresh.isRefreshing) {
                this.swiperefresh.isRefreshing = true
            }
        }

        val database = FirebaseDatabase.getInstance().reference
        val query = database.child(Report.TABLE_NAME).orderByChild(BaseModel.FIELD_DATE)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                aryReport.clear()
                for (reportItem in dataSnapshot.children) {
                    val report = reportItem.getValue(Report::class.java)
                    report?.id = reportItem.key

                    // check existence
                    var bExist = false
                    for (r in aryReport) {
                        if (TextUtils.equals(r.userReportedId, report!!.userReportedId)) {
                            bExist = true
                            break
                        }
                    }

                    if (bExist) {
                        continue
                    }

                    aryReport.add(report!!)

                    // fetch user
                    User.readFromDatabase(report.userReportedId, mFetchUserListner)
                }

                if (aryReport.isEmpty()) {
                    this@AdminReportUserActivity.text_empty_notice.visibility = View.VISIBLE
                    updateList()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                stopRefresh()
            }
        })
    }

    private val mFetchUserListner = object: User.FetchDatabaseListener {
        override fun onFetchedReviews() {
        }

        override fun onFetchedUser(user: User?, success: Boolean) {
            var bFetchedAll = true

            for (reportItem in this@AdminReportUserActivity.aryReport) {
                if (TextUtils.equals(reportItem.userReportedId, user!!.id)) {
                    reportItem.userReported = user
                }

                if (reportItem.userReported == null) {
                    bFetchedAll = false
                }
            }

            if (bFetchedAll) {
                updateList()
            }
        }
    }

    fun updateList() {
        stopRefresh()
        this@AdminReportUserActivity.adapter!!.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == AdminReportDetailActivity.REPORT_DETAIL_CODE) {
            if (resultCode == 1) {
                // deleted report, refresh list
                Handler().postDelayed({ getReports(true, true) }, 500)
            }
        }
    }
}
