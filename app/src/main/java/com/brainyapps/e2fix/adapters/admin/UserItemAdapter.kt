package com.brainyapps.e2fix.adapters.admin

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.activities.BaseUserDetailActivity
import com.brainyapps.e2fix.activities.admin.AdminReportDetail
import com.brainyapps.e2fix.activities.admin.AdminUserDetailActivity
import com.brainyapps.e2fix.adapters.BaseItemAdapter
import com.brainyapps.e2fix.models.User
import com.brainyapps.e2fix.utils.E2FItemClickListener
import com.brainyapps.e2fix.utils.Utils
import com.brainyapps.e2fix.views.ViewHolderLoading
import com.brainyapps.e2fix.views.admin.ViewHolderUserItem
import java.util.ArrayList

/**
 * Created by Administrator on 2/19/18.
 */

class UserItemAdapter(val ctx: Context, val aryUser: ArrayList<User>)
    : BaseItemAdapter(ctx) {

    companion object {
        val ITEM_VIEW_TYPE_USER = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        var vhRes = super.onCreateViewHolder(parent, viewType)

        if (vhRes == null) {
            // create a new view
            val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_user_list_item, parent, false)
            // set the view's size, margins, paddings and layout parameters

            val vh = ViewHolderUserItem(v, ctx)
            vh.setOnItemClickListener(this)
            vhRes = vh
        }

        return vhRes
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder is ViewHolderUserItem) {
            holder.fillContent(this.aryUser[position])
        }
        else {
        }
    }

    override fun getItemCount(): Int {
        var nCount = aryUser.size

        if (mbNeedMore) {
            nCount++
        }

        return nCount
    }

    override fun getItemViewType(position: Int): Int {

        return if (position < aryUser.size) {
            ITEM_VIEW_TYPE_USER
        }
        else {
            ITEM_VIEW_TYPE_FOOTER
        }
    }

    override fun onItemClick(view: View?, position: Int) {
        val id = view!!.id

        val user = aryUser[position]

//        if (user.reported) {
//            val intent = Intent(this.context, AdminReportDetail::class.java)
//            intent.putExtra(BaseUserDetailActivity.KEY_USER, user)
//            this.context!!.startActivity(intent)
//        }
//        else {
            val intent = Intent(this.context, AdminUserDetailActivity::class.java)
            intent.putExtra(BaseUserDetailActivity.KEY_USER, user)
            this.context!!.startActivity(intent)
//        }
    }
}
