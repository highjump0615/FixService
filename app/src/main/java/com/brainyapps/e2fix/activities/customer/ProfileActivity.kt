package com.brainyapps.e2fix.activities.customer

import android.os.Bundle
import com.brainyapps.e2fix.R

class ProfileActivity : BaseCustomerActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_profile)

        setNavbar()
        initDrawer()
    }
}
