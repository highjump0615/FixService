package com.brainyapps.e2fix.models

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.FirebaseDatabase

/**
 * Created by Administrator on 2/21/18.
 */
class Job() : BaseModel(), Parcelable {

    companion object {
        //
        // table info
        //
        const val TABLE_NAME = "jobs"
        const val FIELD_USERID = "userId"


        @JvmField
        val CREATOR: Parcelable.Creator<Job> = object : Parcelable.Creator<Job> {
            override fun createFromParcel(parcel: Parcel): Job {
                return Job(parcel)
            }

            override fun newArray(size: Int): Array<Job?> {
                return arrayOfNulls(size)
            }
        }
    }

    // user posted
    @get:Exclude
    var userPosted: User? = null

    @get:Exclude
    var bidArray: ArrayList<Bid> = ArrayList()

    var userId = ""

    var title = ""
    var description = ""

    var photoUrl = ""
    var category = 0

    constructor(parcel: Parcel) : this() {
        userPosted = parcel.readParcelable(User::class.java.classLoader)
        userId = parcel.readString()
        title = parcel.readString()
        description = parcel.readString()
        photoUrl = parcel.readString()
        category = parcel.readInt()
        parcel.readList(bidArray, Bid::class.java.classLoader)

        readFromParcelBase(parcel)
    }

    fun saveToDatabase(withId: String) {
        this.id = withId

        val database = FirebaseDatabase.getInstance().reference
        database.child(Job.TABLE_NAME).child(withId).setValue(this)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(userPosted, flags)
        parcel.writeString(userId)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(photoUrl)
        parcel.writeInt(category)
        parcel.writeList(bidArray)

        writeToParcelBase(parcel, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

}