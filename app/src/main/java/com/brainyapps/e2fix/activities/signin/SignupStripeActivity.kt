package com.brainyapps.e2fix.activities.signin

import android.os.Bundle
import android.text.TextUtils
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
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_signup_stripe.*

class SignupStripeActivity : BaseActivity(), View.OnClickListener {

    private val TAG = SignupStripeActivity::class.java!!.getSimpleName()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_stripe)

        setNavbar("Payment Information", true)

        this.but_done.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.but_done -> {

                val user = User.currentUser

                // social log in, make it success directly
                if (!TextUtils.isEmpty(user!!.id)) {
                    saveUserData(user.id)
                    return
                }

                Utils.createProgressDialog(this, "Signing up...", "Submitting user credentials")

                // create new user
                FirebaseManager.mAuth.createUserWithEmailAndPassword(user.email, user.password)
                        .addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
                            if (!task.isSuccessful) {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.exception)
                                Utils.createErrorAlertDialog(this, "Signup failed.", task.exception?.localizedMessage!!).show()

                                Utils.closeProgressDialog()

                                return@OnCompleteListener
                            }

                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success")

                            val userId = FirebaseManager.mAuth.currentUser!!.uid

                            // save photo image
                            if (user.photoByteArray != null) {
                                val metadata = StorageMetadata.Builder()
                                        .setContentType("image/jpeg")
                                        .build()
                                val storageReference = FirebaseStorage.getInstance().getReference(User.TABLE_NAME).child(userId + ".jpg")
                                val uploadTask = storageReference.putBytes(user.photoByteArray!!, metadata)
                                uploadTask.addOnSuccessListener(this@SignupStripeActivity, OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
                                    user.photoUrl = taskSnapshot.downloadUrl.toString()
                                    saveUserData(userId)
                                }).addOnFailureListener(this@SignupStripeActivity, OnFailureListener {
                                    Log.d(TAG, it.toString())

                                    saveUserData(userId)
                                })
                            }
                            else {
                                saveUserData(userId)
                            }
                        })
            }
        }
    }

    private fun saveUserData(userId: String) {
        User.currentUser!!.saveToDatabase(userId)
        goToMain()
    }
}
