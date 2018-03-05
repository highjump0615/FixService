package com.brainyapps.e2fix.models

import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.*


/**
 * Created by Administrator on 2/19/18.
 */

class User() : BaseModel(), Parcelable {

    companion object {
        val USER_TYPE_ADMIN = 0
        val USER_TYPE_CUSTOMER = 1
        val USER_TYPE_SERVICEMAN = 2
        val TAG = BaseModel::class.java.getSimpleName()

        var currentUser: User? = null

        fun readFromDatabase(withId: String, fetchListener: FetchDatabaseListener) {

            val database = FirebaseDatabase.getInstance().reference
            val query = database.child(User.TABLE_NAME + "/" + withId)

            // Read from the database
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    val u = dataSnapshot.getValue(User::class.java)
                    u?.id = withId

                    fetchListener.onFetchedUser(u, true)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                    Log.w(TAG, "failed to read from database.", error.toException())
                    fetchListener.onFetchedUser(null, false)
                }
            })
        }

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
    var rating = 0.0

    @get:Exclude
    var posts = ArrayList<Job>()

    @get:Exclude
    var reviews = ArrayList<Review>()

    constructor(parcel: Parcel) : this() {
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
        rating = parcel.readDouble()

        readFromParcelBase(parcel)
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
        parcel.writeDouble(rating)

        writeToParcelBase(parcel, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    /**
     * interface for reading from database
     */
    interface FetchDatabaseListener {
        fun onFetchedUser(user: User?, success: Boolean)
    }
}