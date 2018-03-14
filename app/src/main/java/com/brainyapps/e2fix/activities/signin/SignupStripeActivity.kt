package com.brainyapps.e2fix.activities.signin

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.activities.BaseActivity
import com.brainyapps.e2fix.activities.StripeCardInputActivity
import com.brainyapps.e2fix.activities.customer.JobPostedActivity
import com.brainyapps.e2fix.activities.serviceman.JobAvailableActivity
import com.brainyapps.e2fix.models.StripeSource
import com.brainyapps.e2fix.models.User
import com.brainyapps.e2fix.utils.FirebaseManager
import com.brainyapps.e2fix.utils.Utils
import com.brainyapps.e2fix.views.customer.ViewStripeCardItem
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_signup_stripe.*
import kotlinx.android.synthetic.main.layout_payment_item.*
import kotlinx.android.synthetic.main.layout_payment_item.view.*

class SignupStripeActivity : BaseActivity(), View.OnClickListener {

    private val TAG = SignupStripeActivity::class.java.getSimpleName()
    private val CODE_CARD_INFO = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_stripe)

        setNavbar("Payment Information", true)

        this.but_done.setOnClickListener(this)

        this.but_add_payment.setOnClickListener(this)
        this.layout_card_item.setOnClickListener(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CODE_CARD_INFO) {
            // get stripe source from intent
            val bundle = data?.extras
            bundle?.let {
                val source = it.getParcelable<StripeSource>(StripeCardInputActivity.KEY_STRIPE_SOURCE)
                val viewPaymentItem = this.carditem as ViewStripeCardItem
                viewPaymentItem.fillContent(source)
            }
        }
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

            // payment method
            R.id.but_add_payment, R.id.layout_card_item -> {
                goToCardInput()
            }
        }
    }

    private fun goToCardInput() {
        val intent = Intent(this, StripeCardInputActivity::class.java)
        startActivityForResult(intent, CODE_CARD_INFO)
    }

    private fun saveUserData(userId: String) {
        User.currentUser!!.saveToDatabase(userId)
        goToMain()
    }
}
