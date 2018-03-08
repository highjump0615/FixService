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

    fun saveToDatabase(withId: String? = null) {
        if (!TextUtils.isEmpty(withId)) {
            this.id = withId!!
        }
        else if (TextUtils.isEmpty(this.id)) {
            // generate new id
            val database = FirebaseDatabase.getInstance().reference
            this.id = database.child(Report.TABLE_NAME).push().key
        }

        val database = FirebaseDatabase.getInstance().reference
        database.child(Report.TABLE_NAME).child(this.id).setValue(this)
    }

}