package com.brainyapps.ezfix

import android.os.StrictMode
import android.support.multidex.MultiDexApplication
import android.widget.Toast
import com.brainyapps.ezfix.utils.PrefUtils
import com.brainyapps.ezfix.utils.Utils
import com.facebook.FacebookSdk
import com.google.firebase.FirebaseApp

/**
 * Created by Administrator on 3/7/18.
 */

class MainApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        PrefUtils.init(this)

        if (!Utils.isNetworkAvailable(this)) {
            Toast.makeText(this, "Please check the network connection", Toast.LENGTH_SHORT).show()
        }

        FirebaseApp.initializeApp(this)

        FacebookSdk.sdkInitialize(getApplicationContext())

        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
    }
}
