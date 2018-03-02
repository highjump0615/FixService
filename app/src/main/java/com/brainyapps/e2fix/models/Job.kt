package com.brainyapps.e2fix.models

import com.google.firebase.database.Exclude
import com.google.firebase.database.FirebaseDatabase

/**
 * Created by Administrator on 2/21/18.
 */
class Job : BaseModel() {

    companion object {
        //
        // table info
        //
        const val TABLE_NAME = "jobs"
        const val FIELD_USERID = "userId"
    }

    // user posted
    @get:Exclude
    var userPosted: User? = null
    var userId = ""

    var title = ""
    var description = ""

    var photoUrl = ""
    var category = 0

    fun saveToDatabase(withId: String) {
        this.id = withId

        val database = FirebaseDatabase.getInstance().reference
        database.child(Job.TABLE_NAME).child(withId).setValue(this)
    }
}