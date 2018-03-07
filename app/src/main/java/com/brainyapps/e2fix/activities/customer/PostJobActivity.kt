package com.brainyapps.e2fix.activities.customer

import android.app.Activity
import android.app.ProgressDialog
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
import com.brainyapps.e2fix.activities.GeoLocationHelper
import com.brainyapps.e2fix.activities.PhotoActivityHelper
import com.brainyapps.e2fix.models.Job
import com.brainyapps.e2fix.models.User
import com.brainyapps.e2fix.utils.E2FUpdateImageListener
import com.brainyapps.e2fix.utils.FirebaseManager
import com.brainyapps.e2fix.utils.Utils
import com.bumptech.glide.Glide
import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.layout_content_post.*

class PostJobActivity : BaseDrawerActivity(), E2FUpdateImageListener {

    private val TAG = PostJobActivity::class.java.getSimpleName()

    private var photoHelper: PhotoActivityHelper? = null
    private var locationHelper: GeoLocationHelper? = null
    private var strPhotoUrl = ""

    var progressDlg: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        setNavbar("Post a Job", false)
        initDrawer(User.USER_TYPE_CUSTOMER)

        // init photo
        this.photoHelper = PhotoActivityHelper(this)

        // init spinner
        val adapter = ArrayAdapter.createFromResource(this, R.array.jobtypes_array, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        this.spinner.adapter = adapter

        this.but_photo.setOnClickListener(this)
        this.imgview_photo.setOnClickListener(this)
        this.but_post.setOnClickListener(this)

        // init location
        this.locationHelper = GeoLocationHelper(this, "Posting job needs location for showing distance")
    }

    override fun onClick(view: View?) {
        super.onClick(view)

        when (view?.id) {
            // Photo
            R.id.but_photo, R.id.imgview_photo -> {
                this.photoHelper!!.showMenuDialog()
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
                if (photoHelper!!.byteData != null) {
                    showSaveProgress()

                    val metadata = StorageMetadata.Builder()
                            .setContentType("image/jpeg")
                            .build()
                    val storageReference = FirebaseStorage.getInstance().getReference(Job.TABLE_NAME).child("$strKey.jpg")
                    val uploadTask = storageReference.putBytes(photoHelper!!.byteData!!, metadata)
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

    override fun onResume() {
        super.onResume()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        photoHelper!!.onActivityResult(requestCode, resultCode, data)
    }

    override fun getActivity(): Activity {
        return this
    }

    override fun updatePhotoImageView(byteData: ByteArray) {
        Glide.with(this).load(byteData).into(this.imgview_photo)
        this.but_photo.visibility = View.INVISIBLE
    }

    private fun showSaveProgress() {
        if (progressDlg == null) {
            progressDlg = Utils.createProgressDialog(this, "Submitting Job", "Your job will is being posted right now")
        }
    }

    private fun closeSaveProgress() {
        Utils.closeProgressDialog()
        progressDlg = null
    }

    private fun savePost(withId: String) {
        val newJob = Job()

        newJob.title = this.edit_title.text.toString()
        newJob.description = this.edit_description.text.toString()
        newJob.photoUrl = this.strPhotoUrl
        newJob.category = this.spinner.selectedItemPosition
        newJob.location = User.currentUser!!.location
        newJob.userPosted = User.currentUser
        newJob.userId = User.currentUser!!.id

        // geofire
        val geoFire = GeoFire(FirebaseDatabase.getInstance().getReference(Job.TABLE_NAME))
        if (this.locationHelper!!.location != null) {

            showSaveProgress()

            this.but_post.isEnabled = false

            geoFire.setLocation(withId, GeoLocation(this.locationHelper!!.location!!.latitude, this.locationHelper!!.location!!.longitude)) { key, error ->
                if (error != null) {
                    Log.w(TAG, "setLocation:failure", error.toException())

                    this.but_post.isEnabled = true
                }
                else {
                    newJob.saveToDatabase(withId)
                    User.currentUser!!.posts.add(0, newJob)

                    // go to posted job page
                    Utils.moveNextActivity(this, JobPostedActivity::class.java, true)
                }

                // close progress view
                closeSaveProgress()
            }
        }
        else {
            Toast.makeText(this, "Cannot get current location", Toast.LENGTH_SHORT).show()
        }
    }

}
