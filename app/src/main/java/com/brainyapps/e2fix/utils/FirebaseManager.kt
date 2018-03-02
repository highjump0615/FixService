package com.brainyapps.e2fix.utils

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * Created by Administrator on 2/25/18.
 */
object FirebaseManager {

    var mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private val serverTimeListener = object : ValueEventListener {
        override fun onCancelled(error: DatabaseError?) {
            Utils.ServerOffset = 0.0
        }

        override fun onDataChange(dataSnapshot: DataSnapshot) {
            Utils.ServerOffset = dataSnapshot.getValue<Double>(Double::class.java)!!
        }
    }

    fun initServerTime() {
        val serverTimeQuery = FirebaseDatabase.getInstance().reference.child(".info/serverTimeOffset")
        serverTimeQuery.addValueEventListener(serverTimeListener)
    }

}