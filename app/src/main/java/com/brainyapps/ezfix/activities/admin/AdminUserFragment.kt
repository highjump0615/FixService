package com.brainyapps.ezfix.activities.admin

import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.brainyapps.ezfix.R
import com.brainyapps.ezfix.adapters.admin.UserItemAdapter
import com.brainyapps.ezfix.models.BaseModel
import com.brainyapps.ezfix.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_admin_user.*
import java.util.ArrayList

/**
 * Created by Administrator on 2/19/18.
 */
class AdminUserFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    var adapter: UserItemAdapter? = null
    var aryUser = ArrayList<User>()

    var isInitialized = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_admin_user, container, false)

        // init list
        val refreshView = rootView.findViewById<SwipeRefreshLayout>(R.id.swiperefresh)
        refreshView.setOnRefreshListener(this)

        val recyclerView = rootView.findViewById<RecyclerView>(R.id.list)

        val layoutManager = LinearLayoutManager(this.activity)
        recyclerView.setLayoutManager(layoutManager)

        this.adapter = UserItemAdapter(this.activity!!, this.aryUser)
        recyclerView.setAdapter(this.adapter)
        recyclerView.setItemAnimator(DefaultItemAnimator())

        return rootView
    }

    /**
     * get User data
     */
    fun getUsers(bRefresh: Boolean, bAnimation: Boolean) {

        if (bAnimation) {
            if (!this.swiperefresh.isRefreshing) {
                this.swiperefresh.isRefreshing = true
            }
        }

        val database = FirebaseDatabase.getInstance().reference
        var query = database.child(User.TABLE_NAME).orderByChild(BaseModel.FIELD_DATE)

        if (this.arguments!!.getInt(ARG_USER_LIST_TYPE) == AdminUserActivity.USER_BANNED) {
            query = database.child(User.TABLE_NAME).orderByChild(User.FIELD_BANNED).equalTo(true)
        }

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                stopRefresh()

                var bEmpty = false
                // if empty, use animation for add
                if (aryUser.isEmpty()) {
                    bEmpty = true
                }

                aryUser.clear()

                if (!dataSnapshot.exists()) {
                    this@AdminUserFragment.text_empty_notice.visibility = View.VISIBLE
                }

                for (userItem in dataSnapshot.children) {
                    val user = userItem.getValue(User::class.java)
                    user!!.id = userItem.key
                    if (user.type == User.USER_TYPE_ADMIN) {
                        continue
                    }

                    aryUser.add(user)
                }

                if (bEmpty) {
                    this@AdminUserFragment.adapter!!.notifyItemRangeInserted(0, aryUser.count())
                }
                else {
                    this@AdminUserFragment.adapter!!.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                stopRefresh()
            }
        })

        isInitialized = true
    }

    fun stopRefresh() {
        this.swiperefresh.isRefreshing = false
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private val ARG_USER_LIST_TYPE = "user_list_type"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        fun newInstance(listType: Int): AdminUserFragment {
            val fragment = AdminUserFragment()
            val args = Bundle()
            args.putInt(ARG_USER_LIST_TYPE, listType)
            fragment.arguments = args

            return fragment
        }
    }

    override fun onRefresh() {
        getUsers(true, false)
    }
}