package com.brainyapps.e2fix.activities.serviceman

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.activities.BaseActivity
import com.brainyapps.e2fix.activities.PhotoActivityHelper
import com.brainyapps.e2fix.utils.E2FUpdateImageListener
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_serviceman_edit_profile.*
import java.io.ByteArrayOutputStream

class EditProfileActivity : BaseActivity(), View.OnClickListener, E2FUpdateImageListener {

    var helper: PhotoActivityHelper? = null

    override fun getActivity(): Activity {
        return this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_serviceman_edit_profile)

        this.helper = PhotoActivityHelper(this)

        setNavbar("Edit Profile", true)

        this.imgview_photo.setOnClickListener(this)
        this.but_save.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            // Photo
            R.id.imgview_photo -> {
                this.helper!!.showMenuDialog()
            }

            // save
            R.id.but_save -> {
                finish()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        helper!!.onActivityResult(requestCode, resultCode, data)
    }

    override fun updatePhotoImageView(byteData: ByteArray) {
        Glide.with(this).load(byteData).into(this.imgview_photo)
    }
}
