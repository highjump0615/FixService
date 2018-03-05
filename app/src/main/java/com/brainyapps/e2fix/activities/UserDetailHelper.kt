package com.brainyapps.e2fix.activities

import android.app.Activity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.models.User
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

/**
 * Created by Administrator on 3/2/18.
 */
class UserDetailHelper(private val contentView: View) {

    companion object {
        val KEY_USER = "user"
    }

    init {
    }

    fun fillUserInfo(user: User) {
        //
        // show user info
        //

        // name
        var text = contentView.findViewById<TextView>(R.id.text_name)
        text.text = user.userFullName()

        // photo
        val imgview = contentView.findViewById<ImageView>(R.id.imgview_photo)
        Glide.with(contentView.context)
                .load(user.photoUrl)
                .apply(RequestOptions.placeholderOf(R.drawable.user_default).fitCenter())
                .into(imgview)

        // phone
        text = contentView.findViewById<TextView>(R.id.text_phone)
        text.text = user.contact

        // location
        text = contentView.findViewById<TextView>(R.id.text_location)
        text?.text = user.location

        // skill
        text = contentView.findViewById<TextView>(R.id.text_skill)
        text?.text = user.skill
    }
}