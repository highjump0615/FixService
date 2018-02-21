package com.brainyapps.e2fix.adapters

import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.activities.serviceman.BidSubmitActivity
import com.brainyapps.e2fix.models.Job
import com.brainyapps.e2fix.utils.E2FItemClickListener
import com.brainyapps.e2fix.utils.Utils
import com.brainyapps.e2fix.views.ViewHolderLoading
import com.brainyapps.e2fix.views.admin.ViewHolderUserItem
import com.brainyapps.e2fix.views.serviceman.ViewHolderJobItem
import com.bumptech.glide.util.Util
import java.util.ArrayList

/**
 * Created by Administrator on 2/19/18.
 */

open class BaseItemAdapter()
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(), E2FItemClickListener {

    var mbNeedMore = false

    companion object {
        val ITEM_VIEW_TYPE_FOOTER = 10
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {

        var vhRes: RecyclerView.ViewHolder? = null

        if (viewType == ITEM_VIEW_TYPE_FOOTER) {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_list_loading, parent, false)

            val vh = ViewHolderLoading(v)
            vhRes = vh
        }

        return vhRes
    }

    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemClick(view: View?, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
