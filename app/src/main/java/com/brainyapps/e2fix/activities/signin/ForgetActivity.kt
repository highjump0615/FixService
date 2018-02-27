package com.brainyapps.e2fix.activities.signin

import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.models.User
import com.brainyapps.e2fix.utils.FirebaseManager
import com.brainyapps.e2fix.utils.Utils
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_forget.*

class ForgetActivity : SignupBaseActivity(), View.OnClickListener {

    private val TAG = ForgetActivity::class.java!!.getSimpleName()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView(R.layout.activity_forget, "Forgot Password")

        this.edit.addTextChangedListener(mTextWatcher)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            // submit
            R.id.but_next -> {
                Utils.createProgressDialog(this, "Submitting...", "Sending password reset email")

                FirebaseManager.mAuth.sendPasswordResetEmail(this.edit.text.toString())
                        .addOnCompleteListener(OnCompleteListener { task ->
                            if (!task.isSuccessful) {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "sendPasswordResetEmail:failure", task.exception)
                                Utils.createErrorAlertDialog(this, "Failed Sending", task.exception?.localizedMessage!!).show()
                            }
                            else {
                                Utils.createErrorAlertDialog(this,
                                        "Email Sent",
                                        "We’ve sent a password reset link to your email",
                                        DialogInterface.OnClickListener {dialog, which ->
                                            this@ForgetActivity.finish()
                                        })
                                        .show()
                            }

                            Utils.closeProgressDialog()
                        })
            }
        }
    }

    /**
     * send reset password email
     */
    fun sendResetPassword() {

    }

    private val mTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
        }

        override fun afterTextChanged(editable: Editable) {
            val email = editable.toString()

            enableNextButton(false)
            this@ForgetActivity.chk_registered.setChecked(false)

            if (Utils.isValidEmail(email)) {
                // check email existence
                val database = FirebaseDatabase.getInstance().reference
                val query = database.child(User.TABLE_NAME)

                // Read from the database
                query.orderByChild(User.FIELD_EMAIL)
                        .equalTo(email)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    this@ForgetActivity.chk_registered.setChecked(true)
                                    enableNextButton(true)
                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                            }
                        })
            }

        }
    }
}
