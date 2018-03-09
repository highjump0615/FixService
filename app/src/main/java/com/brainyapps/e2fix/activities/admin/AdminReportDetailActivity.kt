package com.brainyapps.e2fix.activities.admin

import android.os.Bundle
import android.view.View
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.activities.BaseActivity
import com.brainyapps.e2fix.models.Report
import com.brainyapps.e2fix.models.User
import kotlinx.android.synthetic.main.activity_admin_report_detail.*

class AdminReportDetailActivity : BaseActivity(), View.OnClickListener {

    companion object {
        val KEY_REPORT = "report"
        val REPORT_DETAIL_CODE = 100
    }

    var report: Report? = null
    var banHelper: AdminBanUserHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_report_detail)

        // get report from intent
        val bundle = intent.extras
        this.report = bundle?.getParcelable<Report>(KEY_REPORT)

        setNavbar(this.report!!.userReported!!.userFullName(), true)

        this.text_content.text = report!!.content
        this.but_delete.setOnClickListener(this)

        // fetch user info
        User.readFromDatabase(this.report!!.userId, object: User.FetchDatabaseListener {
            override fun onFetchedUser(user: User?, success: Boolean)
            {
                this@AdminReportDetailActivity.text_user_name.text = "Reported by ${user?.userFullName()}"
                this@AdminReportDetailActivity.text_user_name.visibility = View.VISIBLE
            }

            override fun onFetchedReviews() {
            }
        })

        this.banHelper = AdminBanUserHelper(this, this.report!!.userReported!!)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            // delete report
            R.id.but_delete -> {
                this.report?.deleteFromDatabase()

                setResult(1)
                finish()
            }
        }
    }

}
