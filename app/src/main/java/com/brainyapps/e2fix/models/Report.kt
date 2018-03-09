package com.brainyapps.e2fix.models

import android.os.Parcel
import android.os.Parcelable
import android.text.TextUtils
import com.google.firebase.database.Exclude
import com.google.firebase.database.FirebaseDatabase

/**
 * Created by Administrator on 2/19/18.
 */

class Report() : BaseModel(), Parcelable {

    companion object {
        //
        // table info
        //
        const val TABLE_NAME = "reports"

        @JvmField
        val CREATOR = object : Parcelable.Creator<Report> {
            override fun createFromParcel(parcel: Parcel): Report {
                return Report(parcel)
            }

            override fun newArray(size: Int): Array<Report?> {
                return arrayOfNulls(size)
            }
        }
    }

    var userId = ""
    @get:Exclude
    var user: User? = null

    var userReportedId = ""
    @get:Exclude
    var userReported: User? = null

    var content = ""

    constructor(parcel: Parcel) : this() {
        userId = parcel.readString()
        user = parcel.readParcelable(User::class.java.classLoader)
        userReportedId = parcel.readString()
        userReported = parcel.readParcelable(User::class.java.classLoader)
        content = parcel.readString()

        readFromParcelBase(parcel)
    }

    override fun tableName() = TABLE_NAME
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userId)
        parcel.writeParcelable(user, flags)
        parcel.writeString(userReportedId)
        parcel.writeParcelable(userReported, flags)
        parcel.writeString(content)

        writeToParcelBase(parcel, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

}