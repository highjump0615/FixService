package com.brainyapps.e2fix.activities.admin

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.activities.BaseActivity
import com.brainyapps.e2fix.activities.UserDetailHelper
import com.brainyapps.e2fix.models.User
import com.brainyapps.e2fix.utils.Utils
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_admin_user_detail.*

class AdminUserDetailActivity : BaseActivity(), View.OnClickListener {

    private val TAG = AdminUserDetailActivity::class.java.getSimpleName()
    var helper: UserDetailHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_user_detail)

        this.helper = UserDetailHelper(this)

        setNavbar("USER INFO", true)

        // user type
        this.text_user_type.text = "REGISTERD AS " + this.helper!!.user!!.userTypeString().toUpperCase()

        // button
        this.but_ban.setOnClickListener(this)
        this.but_unban.setOnClickListener(this)

        updateButton()
    }

    /**
     * update buttons according to user status
     */
    fun updateButton() {
        if (this.helper!!.user!!.banned) {
            this.but_ban.visibility = View.GONE
            this.but_unban.visibility = View.VISIBLE
        }
        else {
            this.but_ban.visibility = View.VISIBLE
            this.but_unban.visibility = View.GONE
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            // ban
            R.id.but_ban -> {
                Utils.createProgressDialog(this, "Banning User", "Updating user profile...")

                val database = FirebaseDatabase.getInstance().reference
                database.child(User.TABLE_NAME)
                        .child(this.helper!!.user!!.id)
                        .child(User.FIELD_BANNED)
                        .setValue(true)
                        .addOnCompleteListener(this, OnCompleteListener<Void> { task ->
                            if (!task.isSuccessful) {
                                // fail
                                Log.w(TAG, "updatePassword:failure", task.exception)
                                Utils.createErrorAlertDialog(this, "Ban Failed", task.exception?.localizedMessage!!).show()
                            }
                            else {
                                // success
                                this.helper!!.user!!.banned = true

                                Toast.makeText(this, "Banned user successfully", Toast.LENGTH_SHORT).show()
                                updateButton()
                            }

                            Utils.closeProgressDialog()
                        })
            }

            // unban
            R.id.but_unban -> {
                Utils.createProgressDialog(this, "Unbanning User", "Updating user profile...")

                val database = FirebaseDatabase.getInstance().reference
                database.child(User.TABLE_NAME)
                        .child(this.helper!!.user!!.id)
                        .child(User.FIELD_BANNED)
                        .setValue(false)
                        .addOnCompleteListener(this, OnCompleteListener<Void> { task ->
                            if (!task.isSuccessful) {
                                // fail
                                Log.w(TAG, "updatePassword:failure", task.exception)
                                Utils.createErrorAlertDialog(this, "Unban Failed", task.exception?.localizedMessage!!).show()
                            }
                            else {
                                // success
                                this.helper!!.user!!.banned = false

                                Toast.makeText(this, "Unbanned user successfully", Toast.LENGTH_SHORT).show()
                                updateButton()
                            }

                            Utils.closeProgressDialog()
                        })
            }
        }
    }

}
