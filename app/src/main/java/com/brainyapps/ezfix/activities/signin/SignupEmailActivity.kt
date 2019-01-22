package com.brainyapps.ezfix.activities.signin

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.CheckBox
import com.brainyapps.ezfix.R
import com.brainyapps.ezfix.models.User
import com.brainyapps.ezfix.utils.Utils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_signup_email.*

class SignupEmailActivity : SignupBaseActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView(R.layout.activity_signup_email)

        this.edit.addTextChangedListener(mTextWatcher)
    }

    private val mTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
        }

        override fun afterTextChanged(editable: Editable) {
            val email = editable.toString()

            enableNextButton(false)
            this@SignupEmailActivity.chk_valid_email.setChecked(false)
            this@SignupEmailActivity.chk_not_used.setChecked(false)

            if (Utils.isValidEmail(email)) {
                this@SignupEmailActivity.chk_valid_email.setChecked(true)
                checkUsedEmail(email)
            }
        }
    }

    /**
     * check if email has been used
     */
    private fun checkUsedEmail(email: String) {
        val database = FirebaseDatabase.getInstance().reference
        val query = database.child(User.TABLE_NAME)

        // Read from the database
        query.orderByChild(User.FIELD_EMAIL)
                .equalTo(email)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            this@SignupEmailActivity.chk_not_used.setChecked(true)
                            enableNextButton(true)
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                    }
                })
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            // Next
            R.id.but_next -> {
                val intent = Intent(this, SignupPasswordActivity::class.java)
                intent.putExtra(SignupBaseActivity.KEY_USER_TYPE, this.userType)
                intent.putExtra(SignupBaseActivity.KEY_EMAIL, this.edit.text.toString())
                startActivity(intent)
            }
        }
    }
}
