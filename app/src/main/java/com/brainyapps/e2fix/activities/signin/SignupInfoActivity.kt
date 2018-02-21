package com.brainyapps.e2fix.activities.signin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.activities.PhotoActivityHelper
import com.brainyapps.e2fix.utils.E2FUpdateImageListener
import com.brainyapps.e2fix.utils.Utils
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_signup_info.*

class SignupInfoActivity : SignupBaseActivity(), E2FUpdateImageListener {

    var helper: PhotoActivityHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_info)

        this.helper = PhotoActivityHelper(this)

        this.but_photo.setOnClickListener(this)
        this.imgview_photo.setOnClickListener(this)
        this.but_next.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            // Photo
            R.id.but_photo, R.id.imgview_photo -> {
                this.helper!!.showMenuDialog()
            }
            // next
            R.id.but_next -> {
                Utils.moveNextActivity(this, SignupStripeActivity::class.java)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun getActivity(): Activity {
        return this
    }

    override fun updatePhotoImageView(byteData: ByteArray) {
        Glide.with(this).load(byteData).into(this.imgview_photo)
    }
}
