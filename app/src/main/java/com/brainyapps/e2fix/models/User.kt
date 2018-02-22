package com.brainyapps.e2fix.models

/**
 * Created by Administrator on 2/19/18.
 */

class User {

    companion object {
        val USER_TYPE_ADMIN = 0
        val USER_TYPE_CUSTOMER = 1
        val USER_TYPE_SERVICEMAN = 2

        val currentUser: User? = null
    }

    val type: Int = USER_TYPE_ADMIN
}