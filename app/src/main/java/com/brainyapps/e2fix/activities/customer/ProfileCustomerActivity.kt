package com.brainyapps.e2fix.activities.customer

import android.os.Bundle
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.activities.BaseDrawerActivity
import com.brainyapps.e2fix.activities.BaseUserDetailHelper
import com.brainyapps.e2fix.models.User

class ProfileCustomerActivity : BaseDrawerActivity() {

    var helper: BaseUserDetailHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_profile)

        setNavbar()
        initDrawer(User.USER_TYPE_CUSTOMER)

        this.helper = BaseUserDetailHelper(this)
    }
}
