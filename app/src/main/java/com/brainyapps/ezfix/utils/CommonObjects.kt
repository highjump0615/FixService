package com.brainyapps.ezfix.utils

import android.content.Context
import com.brainyapps.ezfix.models.Bid
import com.brainyapps.ezfix.models.Job
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