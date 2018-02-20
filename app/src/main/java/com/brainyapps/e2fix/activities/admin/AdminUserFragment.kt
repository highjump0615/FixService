package com.brainyapps.e2fix.activities.admin

import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.adapters.admin.UserItemAdapter
import com.brainyapps.e2fix.models.User
import java.util.ArrayList

/**
 * Created by Administrator on 2/19/18.
 */
class AdminUserFragment : Fragment() {

    var adapter: UserItemAdapter? = null

    var aryUser = ArrayList<User>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_admin_user, container, false)

        // init data
        for (i in 0..15) {
            this.aryUser.add(User())
        }

        // init list
        val recyclerView = rootView.findViewById<RecyclerView>(R.id.list)

        val layoutManager = LinearLayoutManager(this.activity)
        recyclerView.setLayoutManager(layoutManager)

        this.adapter = UserItemAdapter(this.activity!!, this.aryUser, UserItemAdapter.ITEM_VIEW_TYPE_USER)
        recyclerView.setAdapter(this.adapter)
        recyclerView.setItemAnimator(DefaultItemAnimator())

        return rootView
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        fun newInstance(sectionNumber: Int): AdminUserFragment {
            val fragment = AdminUserFragment()
            val args = Bundle()
            args.putInt(ARG_SECTION_NUMBER, sectionNumber)
            fragment.arguments = args

            return fragment
        }
    }
}