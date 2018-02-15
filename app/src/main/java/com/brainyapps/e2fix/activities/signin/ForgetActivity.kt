package com.brainyapps.e2fix.activities.signin

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.CheckBox
import android.widget.RelativeLayout
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.activities.BaseActivity

class ForgetActivity : BaseActivity(), View.OnClickListener {

    lateinit var mbutSubmit: RelativeLayout

    lateinit var mchkRegistered: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget)

        setNavbar("Forgot Password", true)

        mbutSubmit = findViewById<View>(R.id.but_submit) as RelativeLayout
        mbutSubmit.setOnClickListener(this)

        mchkRegistered = findViewById<View>(R.id.chk_registered) as CheckBox
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            // submit
            R.id.but_submit -> {
            }
        }
    }

    private val mTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            val email = charSequence.toString()

            if (!TextUtils.isEmpty(email)) {
            }
            else {
                mchkRegistered.setChecked(false)
            }
        }

        override fun afterTextChanged(editable: Editable) {

        }
    }
}
