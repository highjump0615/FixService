package com.brainyapps.ezfix.adapters

import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.brainyapps.ezfix.R
import com.brainyapps.ezfix.activities.serviceman.BidSubmitActivity
import com.brainyapps.ezfix.models.Job
import com.brainyapps.ezfix.utils.E2FItemClickListener
import com.brainyapps.ezfix.utils.Utils
import com.brainyapps.ezfix.views.ViewHolderLoading
import com.brainyapps.ezfix.views.admin.ViewHolderUserItem
import com.brainyapps.ezfix.views.serviceman.ViewHolderJobItem
import com.bumptech.glide.util.Util
import java.util.ArrayList

/**
 * Created by Administrator on 2/19/18.
 */

open class BaseItemAdapter(ctx: Context)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(), E2FItemClickListener {

    var context: Context? = null
    var mbNeedMore = false

    companion object {
        val ITEM_VIEW_TYPE_FOOTER = 10
    }

    init {
        this.context = ctx
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
