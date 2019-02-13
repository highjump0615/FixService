package com.brainyapps.ezfix.models

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Administrator on 3/14/18.
 */
class StripeSource() : Parcelable {

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<StripeSource> {
            override fun createFromParcel(parcel: Parcel): StripeSource {
                return StripeSource(parcel)
            }

            override fun newArray(size: Int): Array<StripeSource?> {
                return arrayOfNulls(size)
            }
        }
    }

    var sourceId = ""
    var secretId = ""
    var last4 = ""
    var brand = ""

    constructor(parcel: Parcel) : this() {
        sourceId = parcel.readString()
        secretId = parcel.readString()
        last4 = parcel.readString()
        brand = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(sourceId)
        parcel.writeString(secretId)
        parcel.writeString(last4)
        parcel.writeString(brand)
    }

    override fun describeContents(): Int {
        return 0
    }
}