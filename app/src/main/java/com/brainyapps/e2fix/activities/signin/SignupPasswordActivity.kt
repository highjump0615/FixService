package com.brainyapps.e2fix.activities.signin

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.utils.Utils

class SignupPasswordActivity : SignupBaseActivity(), View.OnClickListener {

    lateinit var mchkLength: CheckBox
    lateinit var mchkUppercase: CheckBox
    lateinit var mchkLowercase: CheckBox
    lateinit var mchkNumber: CheckBox

    lateinit var medit: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView(R.layout.activity_signup_password)

        mchkLength = findViewById<CheckBox>(R.id.chk_length)
        mchkUppercase = findViewById<CheckBox>(R.id.chk_uppercase)
        mchkLowercase = findViewById<CheckBox>(R.id.chk_lowercase)
        mchkNumber = findViewById<CheckBox>(R.id.chk_number)

        medit = findViewById<EditText>(R.id.edit)
        medit.addTextChangedListener(mTextWatcher)
    }

    private val mTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            val password = charSequence.toString()

            if (!TextUtils.isEmpty(password)) {
                if (password.length >= 6) {
                    mchkLength.setChecked(true)
                }
                else {
                    mchkLength.setChecked(false)
                }

                if (Utils.containsNumber(password)) {
                    mchkNumber.setChecked(true)
                }
                else {
                    mchkNumber.setChecked(false)
                }

                val tempLower = password.toLowerCase()
                val hasUppercase = password != tempLower
                if (hasUppercase) {
                    mchkUppercase.setChecked(true)
                } else {
                    mchkUppercase.setChecked(false)
                }

                val tempUpper = password.toUpperCase()
                val hasLowercase = password != tempUpper
                if (hasLowercase) {
                    mchkLowercase.setChecked(true)
                } else {
                    mchkLowercase.setChecked(false)
                }
            }
            else {
                mchkLength.setChecked(false)
                mchkUppercase.setChecked(false)
                mchkLowercase.setChecked(false)
                mchkNumber.setChecked(false)
            }

//            enableNext(password)
        }

        override fun afterTextChanged(editable: Editable) {

        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            // Next
            R.id.but_next -> {
                val intent = Intent(this@SignupPasswordActivity, SignupRePasswordActivity::class.java)
                intent.putExtra(SignupRePasswordActivity.KEY_PASSWORD, medit.text.toString())
                startActivity(intent)
            }
        }
    }
}
