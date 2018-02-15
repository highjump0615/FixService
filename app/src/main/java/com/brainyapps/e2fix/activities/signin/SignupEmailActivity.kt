package com.brainyapps.e2fix.activities.signin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RelativeLayout
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.activities.BaseActivity
import com.brainyapps.e2fix.utils.Utils

class SignupEmailActivity : BaseActivity(), View.OnClickListener {

    lateinit var mbutNext: RelativeLayout
    lateinit var mchkEmail: CheckBox
    lateinit var mchkUsed: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_email)

        setNavbar("Sign up", true)

        mbutNext = findViewById<View>(R.id.but_next) as RelativeLayout
        mbutNext.setOnClickListener(this)

        mchkEmail = findViewById<View>(R.id.chk_valid_email) as CheckBox

        val editEmail = findViewById<EditText>(R.id.edit)
        editEmail.addTextChangedListener(mTextWatcher)
    }

    private val mTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            val email = charSequence.toString()

            if (!TextUtils.isEmpty(email)) {
                if (Utils.isValidEmail(email)) {
//                    butNext.setEnabled(false)
                    mchkEmail.setChecked(true)
//                    checkUsedEmail(email)
                }
                else {
//                    btnNext.setEnabled(false)
                    mchkEmail.setChecked(false)
//                    validEmailIndicator.start()
//                    usedEmailIndicator.start()
                }
            }
            else {
//                btnNext.setEnabled(false)
                mchkEmail.setChecked(false)
            }
        }

        override fun afterTextChanged(editable: Editable) {

        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            // Next
            R.id.but_next -> {
            }
        }
    }
}
