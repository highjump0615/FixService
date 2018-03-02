package com.brainyapps.e2fix.models

import com.brainyapps.e2fix.utils.Utils
import com.google.firebase.database.Exclude
import com.google.firebase.database.ServerValue
import java.util.*

/**
 * Created by Administrator on 2/27/18.
 */
open class BaseModel : Comparable<BaseModel> {

    companion object {
        val FIELD_DATE = "createdAt"
    }

    @get:Exclude
    var id = ""

    @get:Exclude
    var dateCreated: Date? = null

    init {
        this.dateCreated = Utils.getServerTime()
    }

    /**
     * get/set timestamp
     */
    fun getCreatedAt(): Map<String, String>? {
        return ServerValue.TIMESTAMP
    }

    fun setCreatedAt(value: Long) {
        this.dateCreated = Date(value)
    }

    override operator fun compareTo(other: BaseModel): Int {
        return if (this.dateCreated!!.after(other.dateCreated)) {
            1
        } else if (this.dateCreated!!.before(other.dateCreated)) {
            -1
        } else {
            0
        }
    }

}