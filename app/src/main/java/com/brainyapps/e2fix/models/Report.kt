package com.brainyapps.e2fix.models

import android.text.TextUtils
import com.google.firebase.database.Exclude
import com.google.firebase.database.FirebaseDatabase

/**
 * Created by Administrator on 2/19/18.
 */

class Report : BaseModel() {

    companion object {
        //
        // table info
        //
        const val TABLE_NAME = "reports"
    }

    var userId = ""
    @get:Exclude
    var user: User? = null

    var userReportedId = ""
    @get:Exclude
    var userReported: User? = null

    var content = ""

    override fun tableName() = TABLE_NAME

}