package com.brainyapps.e2fix.views.admin

import android.support.v7.widget.RecyclerView
import android.view.View
import com.brainyapps.e2fix.utils.E2FItemClickListener

/**
 * Created by Administrator on 2/19/18.
 */

open class ViewHolderBase(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    internal var mClickListener: E2FItemClickListener? = null

    init {
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
