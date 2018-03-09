package com.brainyapps.e2fix.activities.admin

import android.app.Activity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.models.User
import com.brainyapps.e2fix.utils.Utils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.FirebaseDatabase

/**
 * Created by Administrator on 3/2/18.
 */
class AdminBanUserHelper(val owner: Activity, val user: User) : View.OnClickListener {

    private val TAG = AdminUserDetailActivity::class.java.getSimpleName()

    var butBan: Button? = null
    var butUnban: Button? = null

    init {
        butBan = owner.findViewById(R.id.but_ban)
        butBan!!.setOnClickListener(this)
        butUnban = owner.findViewById(R.id.but_unban)
        butUnban!!.setOnClickListener(this)

        updateButton()
    }

    /**
     * update buttons according to user status
     */
    private fun updateButton() {
        if (user.banned) {
            butBan?.visibility = View.GONE
            butUnban?.visibility = View.VISIBLE
        }
        else {
            butBan?.visibility = View.VISIBLE
            butUnban?.visibility = View.GONE
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            // ban
            R.id.but_ban -> {
                val database = FirebaseDatabase.getInstance().reference
                database.child(User.TABLE_NAME)
                        .child(this.user.id)
                        .child(User.FIELD_BANNED)
                        .setValue(true)
                        .addOnCompleteListener(owner, OnCompleteListener<Void> { task ->
                            if (!task.isSuccessful) {
                                // fail
                                Log.w(TAG, "updateBan:failure", task.exception)
                            }

                            // success
                            this.user.banned = true

                            Toast.makeText(owner, "Banned user successfully", Toast.LENGTH_SHORT).show()
                            updateButton()
                        })
            }

            // unban
            R.id.but_unban -> {
                val database = FirebaseDatabase.getInstance().reference
                database.child(User.TABLE_NAME)
                        .child(this.user.id)
                        .child(User.FIELD_BANNED)
                        .setValue(false)
                        .addOnCompleteListener(owner, OnCompleteListener<Void> { task ->
                            if (!task.isSuccessful) {
                                // fail
                                Log.w(TAG, "updatePassword:failure", task.exception)
                                Utils.createErrorAlertDialog(owner, "Unban Failed", task.exception?.localizedMessage!!).show()

                                return@OnCompleteListener
                            }

                            // success
                            this.user.banned = false

                            Toast.makeText(owner, "Unbanned user successfully", Toast.LENGTH_SHORT).show()
                            updateButton()
                        })
            }
        }
    }
}