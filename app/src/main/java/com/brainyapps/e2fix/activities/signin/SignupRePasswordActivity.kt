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

class SignupRePasswordActivity : SignupBaseActivity() {

    companion object {
        val KEY_PASSWORD = "password"
    }

    var mstrPassword: String = ""

    lateinit var mchkMatch: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView(R.layout.activity_signup_re_password)

        // get password from prev page
        val bundle = intent.extras
        mstrPassword = bundle.getString(KEY_PASSWORD)

        // init view
        mchkMatch = findViewById<CheckBox>(R.id.chk_length)

        val edit = findViewById<EditText>(R.id.edit)
        edit.addTextChangedListener(mTextWatcher)
    }

    private val mTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            val password = charSequence.toString()

            if (!TextUtils.isEmpty(password)) {
                if (TextUtils.equals(mstrPassword, password)) {
                    mchkMatch.setChecked(true)
                } else {
                    mchkMatch.setChecked(false)
                }
            } else {
                mchkMatch.setChecked(false)
            }

//            enableNext(password);
        }

        override fun afterTextChanged(editable: Editable) {
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            // Next
            R.id.but_next -> {
                Utils.moveNextActivity(this, SignupInfoActivity::class.java)
            }
        }
    }
}
