package com.brainyapps.e2fix.models

import com.google.firebase.database.Exclude
import com.google.firebase.database.ServerValue
import java.util.*

/**
 * Created by Administrator on 2/27/18.
 */
open class BaseModel {

    companion object {
        val FILED_DATE = "createdAt"
    }

    @get:Exclude
    var id = ""

    var dateCreated: Date? = null

    /**
     * get/set timestamp
     */
    fun getCreatedAt(): Map<String, String>? {
        return ServerValue.TIMESTAMP
    }

    fun setCreatedAt(value: Long) {
        this.dateCreated = Date(value)
    }

}