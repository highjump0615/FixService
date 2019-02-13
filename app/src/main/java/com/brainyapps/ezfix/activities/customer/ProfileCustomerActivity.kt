package com.brainyapps.ezfix.activities.customer

import android.os.Bundle
import android.view.View
import com.brainyapps.ezfix.R
import com.brainyapps.ezfix.activities.BaseDrawerActivity
import com.brainyapps.ezfix.activities.UserDetailHelper
import com.brainyapps.ezfix.models.User

class ProfileCustomerActivity : BaseDrawerActivity() {

    var helper: UserDetailHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_profile)

        setNavbar()
        initDrawer(User.USER_TYPE_CUSTOMER)

        // user info
        this.helper = UserDetailHelper(findViewById<View>(android.R.id.content))
        this.helper!!.fillUserInfo(User.currentUser!!)
    }
}
