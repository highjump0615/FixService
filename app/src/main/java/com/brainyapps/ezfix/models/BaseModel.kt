package com.brainyapps.ezfix.models

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import android.text.TextUtils
import com.brainyapps.ezfix.utils.Utils
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Exclude
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import java.util.*

/**
 * Created by Administrator on 2/27/18.
 */
open class BaseModel() : Comparable<BaseModel> {

    companion object {
        //
        // table info
        //
        const val FIELD_DATE = "createdAt"
    }

    @get:Exclude
    var id = ""

    var createdAt: Long

    init {
        this.createdAt = Utils.getServerLongTime()
    }

    open fun tableName() = "base"

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

    fun saveToDatabase(withId: String? = null) {
        val database = FirebaseDatabase.getInstance().reference.child(tableName())

        if (!TextUtils.isEmpty(withId)) {
            this.id = withId!!
        }
        else if (TextUtils.isEmpty(this.id)) {
            // generate new id
            this.id = database.push().key
        }
        database.child(this.id).setValue(this)
    }

    fun deleteFromDatabase() {
        val database = FirebaseDatabase.getInstance().reference.child(tableName())
        database.child(this.id).removeValue()
    }
}