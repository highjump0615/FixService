package com.brainyapps.e2fix.activities.admin

import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.activities.BaseActivity
import com.brainyapps.e2fix.adapters.admin.UserItemAdapter
import com.brainyapps.e2fix.models.User
import java.util.ArrayList

class AdminReportUserActivity : BaseActivity() {

    var aryUser = ArrayList<User>()

    var adapter: UserItemAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_report_user)

        setNavbar("REPORTED USERS", true)

        // init data
        for (i in 0..15) {
            this.aryUser.add(User())
        }

        // init list
        val recyclerView = findViewById<RecyclerView>(R.id.list)

        val layoutManager = LinearLayoutManager(this)
        recyclerView.setLayoutManager(layoutManager)

        this.adapter = UserItemAdapter(this, this.aryUser)
        recyclerView.setAdapter(this.adapter)
        recyclerView.setItemAnimator(DefaultItemAnimator())
    }
}
