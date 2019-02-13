package com.brainyapps.ezfix.models

import android.os.Parcel
import android.os.Parcelable
import android.text.TextUtils
import com.firebase.geofire.GeoLocation
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
    var price: Double = 0.0
    var contact = ""

    var isTaken = false

    var jobId = ""
    @get:Exclude
    var job: Job? = null

    var userId = ""
    @get:Exclude
    var user: User? = null

    // location
    var latitude = 300.0
    var longitude = 300.0

    constructor(parcel: Parcel) : this() {
        time = parcel.readString()
        price = parcel.readDouble()
        contact = parcel.readString()
        isTaken = parcel.readByte().toInt() != 0
        jobId = parcel.readString()
        userId = parcel.readString()
        user = parcel.readParcelable(User::class.java.classLoader)
        latitude = parcel.readDouble()
        longitude = parcel.readDouble()

        readFromParcelBase(parcel)
    }

    override fun tableName() = TABLE_NAME

    fun bidGeoLocation() : GeoLocation? {
        if (GeoLocation.coordinatesValid(latitude, longitude)) {
            return GeoLocation(latitude, longitude)
        }

        return null
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(time)
        parcel.writeDouble(price)
        parcel.writeString(contact)
        parcel.writeByte((if (isTaken) 1 else 0).toByte())
        parcel.writeString(jobId)
        parcel.writeString(userId)
        parcel.writeParcelable(user, flags)
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)

        writeToParcelBase(parcel, flags)
    }

    override fun describeContents(): Int {
        return 0
    }
}