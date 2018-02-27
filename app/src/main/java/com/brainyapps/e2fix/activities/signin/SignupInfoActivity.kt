package com.brainyapps.e2fix.activities.signin

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.activities.PhotoActivityHelper
import com.brainyapps.e2fix.models.User
import com.brainyapps.e2fix.utils.E2FUpdateImageListener
import com.brainyapps.e2fix.utils.Utils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_signup_info.*

class SignupInfoActivity : SignupBaseActivity(), E2FUpdateImageListener {

    var helper: PhotoActivityHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView(R.layout.activity_signup_info)

        this.helper = PhotoActivityHelper(this)

        this.but_photo.setOnClickListener(this)
        this.imgview_photo.setOnClickListener(this)
        this.but_next.setOnClickListener(this)

        // hide the skill input when customer
        if (this.userType == User.USER_TYPE_CUSTOMER) {
            this.edit_skill.visibility = View.GONE
        }

        enableNextButton(true)

        // fill user info
        this.edit_firstname.setText(User.currentUser?.firstName)
        this.edit_lastname.setText(User.currentUser?.lastName)

        if (!TextUtils.isEmpty(User.currentUser?.photoUrl)) {
            Glide.with(this)
                    .load(User.currentUser?.photoUrl)
                    .apply(RequestOptions.placeholderOf(R.drawable.user_default).fitCenter())
                    .into(this.imgview_photo)
            this.but_photo.visibility = View.INVISIBLE
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            // Photo
            R.id.but_photo, R.id.imgview_photo -> {
                this.helper!!.showMenuDialog()
            }
            // next
            R.id.but_next -> {
                if (checkValidation()) {
                    // make new user
                    initUser()

                    Utils.moveNextActivity(this, SignupStripeActivity::class.java)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        helper!!.onActivityResult(requestCode, resultCode, data)
    }

    /**
     * E2FUpdateImageListener
     */
    override fun getActivity(): Activity {
        return this
    }

    override fun updatePhotoImageView(byteData: ByteArray) {
        Glide.with(this).load(byteData).into(this.imgview_photo)
        this.but_photo.visibility = View.INVISIBLE
    }

    /**
     * check validation of input
     */
    private fun checkValidation(): Boolean {
        // first name
        if (TextUtils.isEmpty(this.edit_firstname.text.toString())) {
            Utils.createErrorAlertDialog(this,
                    resources.getString(R.string.error_fill_data),
                    "First name cannot be empty",
                    DialogInterface.OnClickListener {dialog, which ->
                        this@SignupInfoActivity.edit_firstname.requestFocus()
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
                        this@SignupInfoActivity.edit_lastname.requestFocus()
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
                        this@SignupInfoActivity.edit_contact.requestFocus()
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
                        this@SignupInfoActivity.edit_location.requestFocus()
                    }
            ).show()
            return false
        }
        // skill
        if (this.userType == User.USER_TYPE_SERVICEMAN && TextUtils.isEmpty(this.edit_skill.text.toString())) {
            Utils.createErrorAlertDialog(this,
                    resources.getString(R.string.error_fill_data),
                    "Skills cannot be empty",
                    DialogInterface.OnClickListener {dialog, which ->
                        this@SignupInfoActivity.edit_skill.requestFocus()
                    }
            ).show()
            return false
        }

        return true
    }

    /**
     * initialize user info
     */
    private fun initUser() {
        val newUser = User()

        newUser.email = this.email!!
        newUser.password = this.password!!
        newUser.type = this.userType
        newUser.firstName = this.edit_firstname.text.toString()
        newUser.lastName = this.edit_lastname.text.toString()
        newUser.contact = this.edit_contact.text.toString()
        newUser.location = this.edit_location.text.toString()
        newUser.photoByteArray = helper!!.byteData

        if (this.userType == User.USER_TYPE_SERVICEMAN) {
            newUser.skill = this.edit_skill.text.toString()
        }

        User.currentUser = newUser
    }
}
