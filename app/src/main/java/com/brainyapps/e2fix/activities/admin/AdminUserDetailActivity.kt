package com.brainyapps.e2fix.activities.admin

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.activities.BaseUserDetailActivity
import com.brainyapps.e2fix.models.User
import com.brainyapps.e2fix.utils.Utils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_admin_user_detail.*

class AdminUserDetailActivity : BaseUserDetailActivity(), View.OnClickListener {

    private val TAG = AdminUserDetailActivity::class.java.getSimpleName()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_user_detail)

        setNavbar("USER INFO", true)

        // user type
        this.text_user_type.setText("REGISTERD AS " + this.user!!.userTypeString().toUpperCase())

        // name
        this.text_name.setText(this.user!!.userFullName())

        // photo
        Glide.with(this)
                .load(this.user!!.photoUrl)
                .apply(RequestOptions.placeholderOf(R.drawable.user_default).fitCenter())
                .into(this.imgview_photo)

        // phone
        this.text_phone.setText(this.user!!.contact)

        // location
        this.text_location.setText(this.user!!.location)

        // button
        this.but_ban.setOnClickListener(this)
        this.but_unban.setOnClickListener(this)

        updateButton()
    }

    /**
     * update buttons according to user status
     */
    fun updateButton() {
        if (this.user!!.banned) {
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
                        .child(this.user!!.id)
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
                                this.user!!.banned = true

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
                        .child(this.user!!.id)
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
                                this.user!!.banned = false

                                Toast.makeText(this, "Unbanned user successfully", Toast.LENGTH_SHORT).show()
                                updateButton()
                            }

                            Utils.closeProgressDialog()
                        })
            }
        }
    }

}
