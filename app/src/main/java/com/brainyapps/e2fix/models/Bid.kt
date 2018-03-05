package com.brainyapps.e2fix.models

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.FirebaseDatabase

/**
 * Created by Administrator on 2/22/18.
 */
class Bid() : BaseModel(), Parcelable {

    companion object {
        //
        // table info
        //
        const val TABLE_NAME = "bids"
        const val FIELD_JOBID = "jobId"
        const val FIELD_USERID = "userId"


        @JvmField
        val CREATOR = object : Parcelable.Creator<Bid> {
            override fun createFromParcel(parcel: Parcel): Bid {
                return Bid(parcel)
            }

            override fun newArray(size: Int): Array<Bid?> {
                return arrayOfNulls(size)
            }
        }
    }

    var time = ""
    var price = ""
    var contact = ""

    var isTaken = false

    var jobId = ""
    @get:Exclude
    var job: Job? = null

    var userId = ""
    @get:Exclude
    var user: User? = null

    constructor(parcel: Parcel) : this() {
        time = parcel.readString()
        price = parcel.readString()
        contact = parcel.readString()
        isTaken = parcel.readByte().toInt() != 0
        jobId = parcel.readString()
        userId = parcel.readString()
        user = parcel.readParcelable(User::class.java.classLoader)

        readFromParcelBase(parcel)
    }

    fun saveToDatabase(withId: String) {
        this.id = withId

        val database = FirebaseDatabase.getInstance().reference
        database.child(Bid.TABLE_NAME).child(withId).setValue(this)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(time)
        parcel.writeString(price)
        parcel.writeString(contact)
        parcel.writeByte((if (isTaken) 1 else 0).toByte())
        parcel.writeString(jobId)
        parcel.writeString(userId)
        parcel.writeParcelable(user, flags)

        writeToParcelBase(parcel, flags)
    }

    override fun describeContents(): Int {
        return 0
    }
}