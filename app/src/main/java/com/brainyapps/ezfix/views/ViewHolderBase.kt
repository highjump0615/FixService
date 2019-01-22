package com.brainyapps.ezfix.views.admin

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import com.brainyapps.ezfix.utils.E2FItemClickListener

/**
 * Created by Administrator on 2/19/18.
 */

open class ViewHolderBase(itemView: View, ctx: Context) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    var context: Context? = null
    internal var mClickListener: E2FItemClickListener? = null

    init {
        this.context = ctx
        itemView.setOnClickListener(this)
    }

    fun setOnItemClickListener(listener: E2FItemClickListener) {
        mClickListener = listener
    }

    override fun onClick(view: View?) {
        if (mClickListener != null) {
            mClickListener!!.onItemClick(view, layoutPosition)
        }
    }


}
