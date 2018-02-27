package com.brainyapps.e2fix.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.activities.admin.AdminMainActivity
import com.brainyapps.e2fix.activities.customer.JobPostedActivity
import com.brainyapps.e2fix.activities.serviceman.JobAvailableActivity
import com.brainyapps.e2fix.models.User
import com.brainyapps.e2fix.utils.FirebaseManager
import com.brainyapps.e2fix.utils.Utils

/**
 * Created by Administrator on 2/14/18.
 */
open class BaseUserDetailActivity : BaseActivity() {

    companion object {
        val KEY_USER = "user"
    }

    var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // get user type from intent
        val bundle = intent.extras
        if (bundle != null) {
            this.user = bundle.getParcelable<User>(KEY_USER)
        }
    }
}