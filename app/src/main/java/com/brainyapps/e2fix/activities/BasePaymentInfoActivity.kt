package com.brainyapps.e2fix.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.activities.admin.AdminMainActivity
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

/**
 * Created by Administrator on 2/14/18.
 */
open class BasePaymentInfoActivity : BaseActivity() {

    private val TAG = BasePaymentInfoActivity::class.java.getSimpleName()

    companion object {
        val KEY_FROM_SIGNUP = "fromSignup"
    }

    var fromSignup = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // get parameter
        val bundle = intent.extras

        fromSignup = bundle.getBoolean(KEY_FROM_SIGNUP)
    }

    fun saveUserInfo() {
        val user = User.currentUser!!

        //
        // for signup case
        //
        if (fromSignup) {
            // social log in, make it success directly
            if (!TextUtils.isEmpty(user.id)) {
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
                            uploadTask.addOnSuccessListener(this@BasePaymentInfoActivity, OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
                                user.photoUrl = taskSnapshot.downloadUrl.toString()
                                saveUserData(userId)
                            }).addOnFailureListener(this@BasePaymentInfoActivity, OnFailureListener {
                                Log.d(TAG, it.toString())

                                saveUserData(userId)
                            })
                        } else {
                            saveUserData(userId)
                        }
                    })
        }
        //
        // for setting case
        //
        else {
            saveUserData(user.id)
        }
    }

    open fun saveUserData(userId: String) {
        // overridden
    }
}