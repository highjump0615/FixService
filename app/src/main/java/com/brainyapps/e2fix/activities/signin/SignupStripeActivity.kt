package com.brainyapps.e2fix.activities.signin

import android.os.Bundle
import android.util.Log
import android.view.View
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.activities.BaseActivity
import com.brainyapps.e2fix.activities.customer.JobPostedActivity
import com.brainyapps.e2fix.activities.serviceman.JobAvailableActivity
import com.brainyapps.e2fix.models.User
import com.brainyapps.e2fix.utils.FirebaseManager
import com.brainyapps.e2fix.utils.Utils
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import kotlinx.android.synthetic.main.activity_signup_stripe.*

class SignupStripeActivity : BaseActivity(), View.OnClickListener {

    private val TAG = SignupRePasswordActivity::class.java!!.getSimpleName()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_stripe)

        setNavbar("Payment Information", true)

        this.but_done.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.but_done -> {

                val progress = Utils.createProgressDialog(this, "Signing up...", "Submitting user credentials")
                val user = User.currentUser

                // create new user
                FirebaseManager.mAuth.createUserWithEmailAndPassword(user!!.email, user.password)
                        .addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
                            if (!task.isSuccessful) {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.exception)
                                Utils.createErrorAlertDialog(this, "Signup failed.", task.exception?.localizedMessage!!).show()

                                progress.dismiss()

                                return@OnCompleteListener
                            }

                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success")

                            user.saveToDatabase(FirebaseManager.mAuth.getCurrentUser()!!.uid)

                            if (user.type == User.USER_TYPE_SERVICEMAN) {
                                Utils.moveNextActivity(this, JobAvailableActivity::class.java, true, true)
                            }
                            else {
                                Utils.moveNextActivity(this, JobPostedActivity::class.java, true, true)
                            }
                        })
            }
        }
    }
}
