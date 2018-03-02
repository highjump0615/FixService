package com.brainyapps.e2fix.activities.customer

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.activities.BaseDrawerActivity
import com.brainyapps.e2fix.activities.PhotoActivityHelper
import com.brainyapps.e2fix.activities.signin.LoginActivity
import com.brainyapps.e2fix.models.Job
import com.brainyapps.e2fix.models.User
import com.brainyapps.e2fix.utils.E2FUpdateImageListener
import com.brainyapps.e2fix.utils.Utils
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.layout_content_post.*
import java.util.*

class PostJobActivity : BaseDrawerActivity(), E2FUpdateImageListener {

    private val TAG = PostJobActivity::class.java!!.getSimpleName()

    var helper: PhotoActivityHelper? = null
    var strPhotoUrl = ""

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
                // check validate
                if (TextUtils.isEmpty(this.edit_title.text.toString())) {
                    Utils.createErrorAlertDialog(this,
                            resources.getString(R.string.error_fill_data),
                            "Title cannot be empty",
                            DialogInterface.OnClickListener { dialog, which ->
                                this.edit_title.requestFocus()
                            }
                    ).show()

                    return
                }
                if (TextUtils.isEmpty(this.edit_description.text.toString())) {
                    Utils.createErrorAlertDialog(this,
                            resources.getString(R.string.error_fill_data),
                            "Description cannot be empty",
                            DialogInterface.OnClickListener { dialog, which ->
                                this.edit_description.requestFocus()
                            }
                    ).show()

                    return
                }

                // generate id
                val database = FirebaseDatabase.getInstance().reference
                val strKey = database.child(Job.TABLE_NAME).push().getKey();

                // save photo image
                if (helper!!.byteData != null) {
                    val metadata = StorageMetadata.Builder()
                            .setContentType("image/jpeg")
                            .build()
                    val storageReference = FirebaseStorage.getInstance().getReference(Job.TABLE_NAME).child("$strKey.jpg")
                    val uploadTask = storageReference.putBytes(helper!!.byteData!!, metadata)
                    uploadTask.addOnSuccessListener(this, OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
                        this.strPhotoUrl = taskSnapshot.downloadUrl.toString()
                        savePost(strKey)
                    }).addOnFailureListener(this, OnFailureListener {
                        Log.d(TAG, it.toString())

                        Toast.makeText(this, "Failed save photo data", Toast.LENGTH_SHORT).show()
                    })
                }
                else {
                    savePost(strKey)
                }
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

    private fun savePost(withId: String) {
        val newJob = Job()

        newJob.title = this.edit_title.text.toString()
        newJob.description = this.edit_description.text.toString()
        newJob.photoUrl = this.strPhotoUrl
        newJob.category = this.spinner.selectedItemPosition
        newJob.userPosted = User.currentUser
        newJob.userId = User.currentUser!!.id

        newJob.saveToDatabase(withId)

        User.currentUser!!.posts.add(newJob)

        // go to posted job page
        Utils.moveNextActivity(this, JobPostedActivity::class.java, true)
    }
}
