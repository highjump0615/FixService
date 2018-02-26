package com.brainyapps.e2fix.activities.admin

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.activities.BaseActivity
import com.brainyapps.e2fix.models.User
import com.brainyapps.e2fix.utils.FirebaseManager
import com.brainyapps.e2fix.utils.Utils
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.EmailAuthProvider
import kotlinx.android.synthetic.main.activity_admin_edit_profile.*
import kotlinx.android.synthetic.main.activity_login.*

class AdminEditProfileActivity : BaseActivity(), View.OnClickListener {

    private val TAG = AdminEditProfileActivity::class.java.getSimpleName()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_edit_profile)

        setNavbar("Edit Owner Profile", true)

        // email
        this.edit_email.isEnabled = false
        this.edit_email.setText(User.currentUser!!.email, TextView.BufferType.EDITABLE)

        this.but_save.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            // save
            R.id.but_save -> {
                val strPassword = this.edit_password.text.toString()
                val strCurPassword = this.edit_password_current.text.toString()
                if (TextUtils.isEmpty(strPassword) || TextUtils.isEmpty(strPassword)) {
                    Utils.createErrorAlertDialog(this, "Input Password", "Current and new password cannot be empty").show()
                    return
                }

                // check confirm password
                if (!TextUtils.equals(this.edit_repassword.text.toString(), strPassword)) {
                    Utils.createErrorAlertDialog(this, "Password Mismatch", "Confirm password does not match").show()
                    return
                }

                Utils.createProgressDialog(this, "Updating Profile", "Saving your user information")

                // reauthenticate
                val credential = EmailAuthProvider.getCredential(User.currentUser!!.email, strCurPassword)
                FirebaseManager.mAuth.currentUser!!.reauthenticate(credential).addOnCompleteListener(OnCompleteListener<Void> { task ->
                    if (!task.isSuccessful) {
                        Toast.makeText(this, getString(R.string.error_oldpassword), Toast.LENGTH_SHORT).show()

                        Utils.closeProgressDialog()
                        return@OnCompleteListener
                    }

                    // reset password
                    FirebaseManager.mAuth.currentUser!!.updatePassword(strPassword)
                            .addOnCompleteListener(this, OnCompleteListener<Void> { task ->
                                if (!task.isSuccessful) {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "updatePassword:failure", task.exception)
                                    Utils.createErrorAlertDialog(this, "Save Failed", task.exception?.localizedMessage!!).show()
                                    Utils.closeProgressDialog()

                                    return@OnCompleteListener
                                }

                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "updatePassword:success")
                                finish()
                            })
                })
            }
        }
    }
}
