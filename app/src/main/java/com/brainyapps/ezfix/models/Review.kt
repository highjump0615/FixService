package com.brainyapps.ezfix.models

import com.google.firebase.database.Exclude

/**
 * Created by Administrator on 2/19/18.
 */

class Review : BaseModel() {

    companion object {
        //
        // table info
        //
        const val TABLE_NAME = "reviews"
        const val FIELD_USERID_RATED = "userRatedId"
    }

    var userId = ""
    @get:Exclude
    var user: User? = null

    var userRatedId = ""
    @get:Exclude
    var userRated: User? = null

    var rate = 0.0
    var review = ""

    override fun tableName() = TABLE_NAME
}