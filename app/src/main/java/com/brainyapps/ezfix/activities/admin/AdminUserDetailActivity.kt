package com.brainyapps.ezfix.activities.admin

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.brainyapps.ezfix.R
import com.brainyapps.ezfix.activities.BaseActivity
import com.brainyapps.ezfix.activities.UserDetailHelper
import com.brainyapps.ezfix.models.User
import com.brainyapps.ezfix.utils.Utils
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_admin_user_detail.*

class AdminUserDetailActivity : BaseActivity() {

    private val TAG = AdminUserDetailActivity::class.java.getSimpleName()
    var userHelper: UserDetailHelper? = null
    var banHelper: AdminBanUserHelper? = null
    var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_user_detail)

        setNavbar("USER INFO", true)

        // get user from intent
        val bundle = intent.extras
        this.user = bundle?.getParcelable<User>(UserDetailHelper.KEY_USER)

        // user type
        this.text_user_type.text = "REGISTERD AS " + this.user!!.userTypeString().toUpperCase()

        // user info
        this.userHelper = UserDetailHelper(findViewById<View>(android.R.id.content))
        this.userHelper!!.fillUserInfo(this.user!!)

        this.banHelper = AdminBanUserHelper(this, this.user!!)
    }

}
