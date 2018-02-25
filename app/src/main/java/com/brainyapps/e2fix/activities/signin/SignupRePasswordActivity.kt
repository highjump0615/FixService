package com.brainyapps.e2fix.activities.signin

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import com.brainyapps.e2fix.R
import kotlinx.android.synthetic.main.activity_signup_re_password.*


class SignupRePasswordActivity : SignupBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView(R.layout.activity_signup_re_password)

        // init view
        this.edit.addTextChangedListener(mTextWatcher)
    }

    private val mTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
        }

        override fun afterTextChanged(editable: Editable) {
            val password = editable.toString()

            if (!TextUtils.isEmpty(password)) {
                if (TextUtils.equals(this@SignupRePasswordActivity.password, password)) {
                    chk_match.setChecked(true)
                    enableNextButton(true)
                }
                else {
                    chk_match.setChecked(false)
                }
            } else {
                chk_match.setChecked(false)
            }
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            // Next
            R.id.but_next -> {
                // create new user
                val intent = Intent(this, SignupInfoActivity::class.java)
                intent.putExtra(SignupBaseActivity.KEY_USER_TYPE, this.userType)
                intent.putExtra(SignupBaseActivity.KEY_EMAIL, this.email)
                intent.putExtra(SignupBaseActivity.KEY_PASSWORD, this.password)
                startActivity(intent)
            }
        }
    }
}
