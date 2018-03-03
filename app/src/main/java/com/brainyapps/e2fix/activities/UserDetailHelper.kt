package com.brainyapps.e2fix.activities

import android.app.Activity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.models.User
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

/**
 * Created by Administrator on 3/2/18.
 */
class UserDetailHelper(val owner: Activity) {

    companion object {
        val KEY_USER = "user"
    }

    var user: User? = null

    init {
        val bundle = owner.intent.extras

        // get user from intent
        this.user = bundle?.getParcelable<User>(KEY_USER)
        if (this.user == null) {
            this.user = User.currentUser
        }

        //
        // show user info
        //

        // name
        var text = owner.findViewById<TextView>(R.id.text_name)
        text.text = this.user!!.userFullName()

        // photo
        val imgview = owner.findViewById<ImageView>(R.id.imgview_photo)
        Glide.with(owner)
                .load(this.user!!.photoUrl)
                .apply(RequestOptions.placeholderOf(R.drawable.user_default).fitCenter())
                .into(imgview)

        // phone
        text = owner.findViewById<TextView>(R.id.text_phone)
        text.text = this.user!!.contact

        // location
        text = owner.findViewById<TextView>(R.id.text_location)
        text.text = this.user!!.location
    }
}