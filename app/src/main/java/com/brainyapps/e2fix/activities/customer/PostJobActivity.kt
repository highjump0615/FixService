package com.brainyapps.e2fix.activities.customer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.activities.BaseDrawerActivity
import com.brainyapps.e2fix.activities.PhotoActivityHelper
import com.brainyapps.e2fix.models.User
import com.brainyapps.e2fix.utils.E2FUpdateImageListener
import com.brainyapps.e2fix.utils.Utils
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.layout_content_post.*

class PostJobActivity : BaseDrawerActivity(), E2FUpdateImageListener {

    var helper: PhotoActivityHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        setNavbar("Post a Job", false)
        initDrawer(User.USER_TYPE_CUSTOMER)

        // init photo
        this.helper = PhotoActivityHelper(this)

        // init spinner
        val adapter = ArrayAdapter.createFromResource(this, R.array.jobtypes_array, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        this.spinner.adapter = adapter

        this.but_photo.setOnClickListener(this)
        this.imgview_photo.setOnClickListener(this)
        this.but_post.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        super.onClick(view)

        when (view?.id) {
            // Photo
            R.id.but_photo, R.id.imgview_photo -> {
                this.helper!!.showMenuDialog()
            }
            // next
            R.id.but_post -> {
                Utils.moveNextActivity(this, JobPostedActivity::class.java, true)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        helper!!.onActivityResult(requestCode, resultCode, data)
    }

    override fun getActivity(): Activity {
        return this
    }

    override fun updatePhotoImageView(byteData: ByteArray) {
        Glide.with(this).load(byteData).into(this.imgview_photo)
    }
}
