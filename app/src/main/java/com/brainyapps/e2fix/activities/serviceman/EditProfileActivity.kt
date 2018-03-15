package com.brainyapps.e2fix.activities.serviceman

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.activities.BaseEditProfileActivity
import com.brainyapps.e2fix.activities.PhotoActivityHelper
import com.brainyapps.e2fix.models.User
import com.brainyapps.e2fix.utils.E2FUpdateImageListener
import com.brainyapps.e2fix.utils.Utils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_serviceman_edit_profile.*

class EditProfileActivity : BaseEditProfileActivity(), View.OnClickListener, E2FUpdateImageListener {

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

        initUserInfo()

        val user = User.currentUser!!

        // photo
        Glide.with(this)
                .load(user.photoUrl)
                .apply(RequestOptions.placeholderOf(R.drawable.user_default).fitCenter())
                .into(this.imgview_photo)

        // first name
        this.edit_firstname.setText(user.firstName)

        // lasts name
        this.edit_lastname.setText(user.lastName)

        // contact
        this.edit_contact.setText(user.contact)

        // location
        this.edit_location.setText(user.location)

        // hide the skill input when customer
        this.edit_skill.setText(user.skill)
        if (User.currentUser!!.type == User.USER_TYPE_CUSTOMER) {
            this.edit_skill.visibility = View.GONE
        }

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
                if (!checkPasswordValidate()) {
                    return
                }

                if (!checkInfoValidation()) {
                    return
                }

                Utils.createProgressDialog(this, "Updating Profile", "Saving your user information")

                // save password
                if (!TextUtils.isEmpty(this.editPassword!!.text.toString())) {
                    savePassword(object: PasswordSaveListener {
                        override fun onSavedPassword(success: Boolean) {
                            if (success) {
                                saveUserInfo()
                            }
                            else {
                                Utils.closeProgressDialog()
                            }
                        }
                    })
                }
                else {
                    saveUserInfo()
                }
            }
        }
    }

    /**
     * save user profile information
     */
    fun saveUserInfo() {
        val user = User.currentUser!!

        user.firstName = this.edit_firstname.text.toString()
        user.lastName = this.edit_lastname.text.toString()
        user.contact = this.edit_contact.text.toString()
        user.location = this.edit_location.text.toString()

        if (helper!!.byteData != null) {
            user.photoByteArray = helper!!.byteData
        }

        if (user.type == User.USER_TYPE_SERVICEMAN) {
            user.skill = this.edit_skill.text.toString()
        }

        user.saveToDatabase(user.id)

        Utils.closeProgressDialog()
        finish()
    }

    /**
     * check validation of input
     */
    private fun checkInfoValidation(): Boolean {
        // first name
        if (TextUtils.isEmpty(this.edit_firstname.text.toString())) {
            Utils.createErrorAlertDialog(this,
                    resources.getString(R.string.error_fill_data),
                    "First name cannot be empty",
                    DialogInterface.OnClickListener { dialog, which ->
                        this.edit_firstname.requestFocus()
                    }
            ).show()
            return false
        }
        // last name
        if (TextUtils.isEmpty(this.edit_lastname.text.toString())) {
            Utils.createErrorAlertDialog(this,
                    resources.getString(R.string.error_fill_data),
                    "Last name cannot be empty",
                    DialogInterface.OnClickListener {dialog, which ->
                        this.edit_lastname.requestFocus()
                    }
            ).show()
            return false
        }
        // contact
        if (TextUtils.isEmpty(this.edit_contact.text.toString())) {
            Utils.createErrorAlertDialog(this,
                    resources.getString(R.string.error_fill_data),
                    "Contact information cannot be empty",
                    DialogInterface.OnClickListener {dialog, which ->
                        this.edit_contact.requestFocus()
                    }
            ).show()
            return false
        }
        // location
        if (TextUtils.isEmpty(this.edit_location.text.toString())) {
            Utils.createErrorAlertDialog(this,
                    resources.getString(R.string.error_fill_data),
                    "Location cannot be empty",
                    DialogInterface.OnClickListener {dialog, which ->
                        this.edit_location.requestFocus()
                    }
            ).show()
            return false
        }
        // skill
        if (User.currentUser!!.type == User.USER_TYPE_SERVICEMAN && TextUtils.isEmpty(this.edit_skill.text.toString())) {
            Utils.createErrorAlertDialog(this,
                    resources.getString(R.string.error_fill_data),
                    "Skills cannot be empty",
                    DialogInterface.OnClickListener {dialog, which ->
                        this.edit_skill.requestFocus()
                    }
            ).show()
            return false
        }

        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        helper!!.onActivityResult(requestCode, resultCode, data)
    }

    override fun updatePhotoImageView(byteData: ByteArray) {
        Glide.with(this).load(byteData).into(this.imgview_photo)
    }
}
