package com.brainyapps.e2fix.models

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import com.brainyapps.e2fix.utils.Utils
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Exclude
import com.google.firebase.database.ServerValue
import java.util.*

/**
 * Created by Administrator on 2/27/18.
 */
open class BaseModel() : Comparable<BaseModel> {

    companion object {
        val FIELD_DATE = "createdAt"
    }

    @get:Exclude
    var id = ""

    var createdAt: Long

    init {
        this.createdAt = Utils.getServerLongTime()
    }

    override operator fun compareTo(other: BaseModel): Int {
        return if (this.createdAt > other.createdAt) {
            1
        } else if (this.createdAt < other.createdAt) {
            -1
        } else {
            0
        }
    }

    fun readFromParcelBase(parcel: Parcel) {
        id = parcel.readString()
        createdAt = parcel.readLong()
    }

    fun writeToParcelBase(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeLong(createdAt)
    }

    fun saveToDatabaseBase(node: DatabaseReference) {
        node.child(FIELD_DATE).setValue(this.createdAt)
    }
}