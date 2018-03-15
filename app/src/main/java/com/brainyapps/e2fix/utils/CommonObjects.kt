package com.brainyapps.e2fix.utils

import android.content.Context
import com.brainyapps.e2fix.models.Bid
import com.brainyapps.e2fix.models.Job
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * Created by Administrator on 2/25/18.
 */
object CommonObjects {

    var selectedJob: Job? = null
    var selectedBid: Bid? = null
}