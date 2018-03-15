package com.brainyapps.e2fix.adapters.admin

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.activities.admin.AdminReportDetailActivity
import com.brainyapps.e2fix.adapters.BaseItemAdapter
import com.brainyapps.e2fix.models.Report
import com.brainyapps.e2fix.views.admin.ViewHolderUserItem
import java.util.ArrayList

/**
 * Created by Administrator on 2/19/18.
 */

class ReportItemAdapter(val ctx: Context, private val aryReport: ArrayList<Report>)
    : BaseItemAdapter(ctx) {

    var report: Report? = null

    companion object {
        val ITEM_VIEW_TYPE_REPORT = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        var vhRes = super.onCreateViewHolder(parent, viewType)

        if (vhRes == null) {
            // create a new view
            val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_user_list_item, parent, false)
            // set the view's size, margins, paddings and layout parameters

            val vh = ViewHolderUserItem(v, ctx)
            vh.setOnItemClickListener(this)
            vh.showReportMark()
            vhRes = vh
        }

        return vhRes
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder is ViewHolderUserItem) {
            holder.fillContent(this.aryReport[position].userReported!!)
        }
        else {
        }
    }

    override fun getItemCount(): Int {
        var nCount = aryReport.size

        if (mbNeedMore) {
            nCount++
        }

        return nCount
    }

    override fun getItemViewType(position: Int): Int {

        return if (position < aryReport.size) {
            ITEM_VIEW_TYPE_REPORT
        }
        else {
            ITEM_VIEW_TYPE_FOOTER
        }
    }

    override fun onItemClick(view: View?, position: Int) {
        val id = view!!.id

        val report = aryReport[position]

        val intent = Intent(this.context, AdminReportDetailActivity::class.java)
        intent.putExtra(AdminReportDetailActivity.KEY_REPORT, report)
        val activity = this.context as Activity
        activity.startActivityForResult(intent, AdminReportDetailActivity.REPORT_DETAIL_CODE)
    }
}
