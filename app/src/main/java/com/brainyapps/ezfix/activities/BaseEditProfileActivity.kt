package com.brainyapps.ezfix.activities

import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.brainyapps.ezfix.R
import com.brainyapps.ezfix.models.User
import com.brainyapps.ezfix.utils.FirebaseManager
import com.brainyapps.ezfix.utils.Utils
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.EmailAuthProvider

/**
 * Created by Administrator on 3/2/18.
 */

open class BaseEditProfileActivity : BaseActivity() {

    private val TAG = BaseEditProfileActivity::class.java.getSimpleName()

    var editPasswordCurrent: EditText? = null
    var editPassword: EditText? = null
    var editPasswordConfirm: EditText? = null

    fun initUserInfo() {
        val edit = findViewById<EditText>(R.id.edit_email)
        edit.isEnabled = false
        edit.setText(User.currentUser!!.email)

        // password
        editPasswordCurrent = findViewById(R.id.edit_password_current)
        editPassword = findViewById(R.id.edit_password)
        editPasswordConfirm = findViewById(R.id.edit_repassword)
    }

    fun checkPasswordValidate(): Boolean {
        val strPassword = this.editPassword!!.text.toString()
        val strCurPassword = this.editPasswordCurrent!!.text.toString()

        if (TextUtils.isEmpty(strPassword)) {
            return true
        }

        if (!TextUtils.isEmpty(strPassword) && TextUtils.isEmpty(strCurPassword)) {
            Utils.createErrorAlertDialog(this, "Input Password", "Current password cannot be empty").show()
            return false
        }

        // check confirm password
        if (!TextUtils.equals(this.editPasswordConfirm!!.text.toString(), strPassword)) {
            Utils.createErrorAlertDialog(this, "Password Mismatch", "Confirm password does not match").show()
            return false
        }

        return true
    }

    fun savePassword(saveListener: PasswordSaveListener) {

        val strPassword = this.editPassword!!.text.toString()
        val strCurPassword = this.editPasswordCurrent!!.text.toString()

        // reauthenticate
        val credential = EmailAuthProvider.getCredential(User.currentUser!!.email, strCurPassword)
        FirebaseManager.mAuth.currentUser!!.reauthenticate(credential).addOnCompleteListener(OnCompleteListener<Void> { task ->
            if (!task.isSuccessful) {
                Toast.makeText(this, getString(R.string.error_oldpassword), Toast.LENGTH_SHORT).show()

                saveListener.onSavedPassword(false)
                return@OnCompleteListener
            }

            // reset password
            FirebaseManager.mAuth.currentUser!!.updatePassword(strPassword)
                    .addOnCompleteListener(this, OnCompleteListener<Void> { task ->
                        if (!task.isSuccessful) {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "updatePassword:failure", task.exception)
                            Utils.createErrorAlertDialog(this, "Save Failed", task.exception?.localizedMessage!!).show()

                            saveListener.onSavedPassword(false)

                            return@OnCompleteListener
                        }

                        Log.d(TAG, "updatePassword:success")
                        saveListener.onSavedPassword(true)
                    })
        })
    }


    /**
     * interface for save password
     */
    interface PasswordSaveListener {
        fun onSavedPassword(success: Boolean)
    }
}