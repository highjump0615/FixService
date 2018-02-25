package com.brainyapps.e2fix.models

import com.google.firebase.database.Exclude
import com.google.firebase.database.FirebaseDatabase

/**
 * Created by Administrator on 2/19/18.
 */

class User {

    companion object {
        val USER_TYPE_ADMIN = 0
        val USER_TYPE_CUSTOMER = 1
        val USER_TYPE_SERVICEMAN = 2

        var currentUser: User? = null

        // table info
        val TABLE_NAME = "users"
    }

    var type: Int = USER_TYPE_ADMIN

    @get:Exclude
    var id = ""

    @get:Exclude
    var password = ""

    var email = ""

    var facebookId = ""

    var firstName = ""
    var lastName = ""
    var photoUrl = ""

    var contact = ""
    var location = ""

    var skill = ""

    fun saveToDatabase(withId: String) {
        this.id = withId

        val database = FirebaseDatabase.getInstance().getReference()
        database.child(User.TABLE_NAME).child(withId).setValue(this)
    }
}