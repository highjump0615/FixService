package com.brainyapps.e2fix.models

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.database.*


/**
 * Created by Administrator on 2/19/18.
 */

class User() : BaseModel(), Parcelable {

    companion object {
        val USER_TYPE_ADMIN = 0
        val USER_TYPE_CUSTOMER = 1
        val USER_TYPE_SERVICEMAN = 2

        var currentUser: User? = null

        //
        // table info
        //
        val TABLE_NAME = "users"
        val FIELD_EMAIL = "email"
        val FIELD_TYPE = "type"
        val FIELD_BANNED = "banned"


        @JvmField
        val CREATOR: Parcelable.Creator<User> = object : Parcelable.Creator<User> {
            override fun createFromParcel(parcel: Parcel): User {
                return User(parcel)
            }

            override fun newArray(size: Int): Array<User?> {
                return arrayOfNulls(size)
            }
        }
    }

    var type: Int = USER_TYPE_ADMIN
    var banned: Boolean = false

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

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        type = parcel.readInt()
        banned = parcel.readByte().toInt() != 0
        password = parcel.readString()
        photoByteArray = parcel.createByteArray()
        email = parcel.readString()
        facebookId = parcel.readString()
        firstName = parcel.readString()
        lastName = parcel.readString()
        photoUrl = parcel.readString()
        contact = parcel.readString()
        location = parcel.readString()
        skill = parcel.readString()
    }

    constructor(id: String) : this() {
        this.id = id
    }

    fun saveToDatabase(withId: String) {
        this.id = withId

        val database = FirebaseDatabase.getInstance().reference
        database.child(User.TABLE_NAME).child(withId).setValue(this)
    }

    /**
     * returns type name
     */
    fun userTypeString(): String {
        return if (type == User.USER_TYPE_CUSTOMER) {
            "Customer"
        }
        else {
            "Serviceman"
        }
    }

    /**
     * returns full name
     */
    fun userFullName(): String {
        return "${this.firstName} ${this.lastName}"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeInt(type)
        parcel.writeByte((if (banned) 1 else 0).toByte())
        parcel.writeString(password)
        parcel.writeByteArray(photoByteArray)
        parcel.writeString(email)
        parcel.writeString(facebookId)
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeString(photoUrl)
        parcel.writeString(contact)
        parcel.writeString(location)
        parcel.writeString(skill)
    }

    override fun describeContents(): Int {
        return 0
    }
}