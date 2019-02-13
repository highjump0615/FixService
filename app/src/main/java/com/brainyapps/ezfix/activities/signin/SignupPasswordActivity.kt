package com.brainyapps.ezfix.activities.signin

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import com.brainyapps.ezfix.R
import com.brainyapps.ezfix.utils.Utils
import kotlinx.android.synthetic.main.activity_signup_email.*

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
        }

        override fun afterTextChanged(editable: Editable) {
            val password = editable.toString()

            var nCondition = 0;
            enableNextButton(false)

            if (!TextUtils.isEmpty(password)) {
                if (password.length >= 6) {
                    mchkLength.setChecked(true)
                    nCondition++
                }
                else {
                    mchkLength.setChecked(false)
                }

                if (Utils.containsNumber(password)) {
                    mchkNumber.setChecked(true)
                    nCondition++
                }
                else {
                    mchkNumber.setChecked(false)
                }

                val tempLower = password.toLowerCase()
                val hasUppercase = password != tempLower
                if (hasUppercase) {
                    mchkUppercase.setChecked(true)
                    nCondition++
                } else {
                    mchkUppercase.setChecked(false)
                }

                val tempUpper = password.toUpperCase()
                val hasLowercase = password != tempUpper
                if (hasLowercase) {
                    mchkLowercase.setChecked(true)
                    nCondition++
                } else {
                    mchkLowercase.setChecked(false)
                }

                if (nCondition >= 4) {
                    enableNextButton(true)
                }
            }
            else {
                mchkLength.setChecked(false)
                mchkUppercase.setChecked(false)
                mchkLowercase.setChecked(false)
                mchkNumber.setChecked(false)
            }
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            // Next
            R.id.but_next -> {
                val intent = Intent(this, SignupRePasswordActivity::class.java)
                intent.putExtra(SignupBaseActivity.KEY_PASSWORD, medit.text.toString())
                intent.putExtra(SignupBaseActivity.KEY_USER_TYPE, this.userType)
                intent.putExtra(SignupBaseActivity.KEY_EMAIL, this.email)
                startActivity(intent)
            }
        }
    }
}
