package com.brainyapps.e2fix.models

import com.google.firebase.database.*


/**
 * Created by Administrator on 2/19/18.
 */

class User : BaseModel {

    companion object {
        val USER_TYPE_ADMIN = 0
        val USER_TYPE_CUSTOMER = 1
        val USER_TYPE_SERVICEMAN = 2

        val USER_STATUS_NORMAL = ""
        val USER_STATUS_REPORTED = "reported"
        val USER_STATUS_BANNED = "banned"

        var currentUser: User? = null

        // table info
        val TABLE_NAME = "users"
        val FIELD_EMAIL = "email"
        val FIELD_TYPE = "type"
        val FIELD_STATUS = "status"
    }

    constructor() {
    }

    constructor(id: String) {
        this.id = id
    }

    var type: Int = USER_TYPE_ADMIN
    var status: String = USER_STATUS_NORMAL

    @get:Exclude
    var password = ""

    @get:Exclude
    var photoByteArray: ByteArray? = null

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

        val database = FirebaseDatabase.getInstance().reference
        database.child(User.TABLE_NAME).child(withId).setValue(this)
    }

    /**
     * returns type name
     */
    fun getTypeString(): String {
        return if (type == User.USER_TYPE_CUSTOMER) {
            "Customer"
        }
        else {
            "Serviceman"
        }
    }
}