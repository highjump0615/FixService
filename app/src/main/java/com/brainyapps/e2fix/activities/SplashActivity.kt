package com.brainyapps.e2fix.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.TextureView
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.activities.signin.LoginActivity
import com.brainyapps.e2fix.models.User
import com.brainyapps.e2fix.utils.FirebaseManager
import com.brainyapps.e2fix.utils.Utils


class SplashActivity : BaseActivity() {

    private val TAG = SplashActivity::class.java.getSimpleName()

    private val SPLASH_TIME_OUT = 3000
    private var bTimeUp = false
    private var bFetchedUser = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // init firebase setting
        FirebaseManager.initServerTime()

        // check login state
        val userId = FirebaseManager.mAuth.currentUser?.uid
        if (TextUtils.isEmpty(userId)) {
            Log.d(TAG, "fetched user")
            bFetchedUser = true
        }
        else {
            User.readFromDatabase(userId!!, object: User.FetchDatabaseListener {
                override fun onFetchedReviews() {
                }

                override fun onFetchedUser(user: User?, success: Boolean) {
                    User.currentUser = user

                    Log.d(TAG, "fetched user")
                    bFetchedUser = true

                    // do action only when time is up
                    if (bTimeUp) {
                        goToMainPage()
                    }
                }
            })
        }

        Handler().postDelayed({
            // This method will be executed once the timer is over
            // Start your app main activity

            Log.d(TAG, "time up")
            bTimeUp = true

            // do action only when user is determined
            if (bFetchedUser) {
                goToMainPage()
            }

        }, SPLASH_TIME_OUT.toLong())
    }

    fun goToMainPage() {
        if (User.currentUser != null) {
            goToMain()
        }
        else {
            Utils.moveNextActivity(this, LoginActivity::class.java, true)
        }
    }
}
