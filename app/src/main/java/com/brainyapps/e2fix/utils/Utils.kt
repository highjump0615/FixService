package com.brainyapps.e2fix.utils

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.text.TextUtils

/**
 * Created by Administrator on 2/15/18.
 */
class Utils {

    companion object {

        val PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 9009

        fun isValidEmail(target: String): Boolean {
            return if (TextUtils.isEmpty(target)) {
                false
            } else {
                android.util.Patterns.EMAIL_ADDRESS.matcher(target)
                        .matches()
            }
        }

        fun containsCharacter(string: String): Boolean {
            return string.matches(".*[a-zA-Z].*".toRegex())
        }

        fun containsNumber(string: String): Boolean {
            return string.matches(".*\\d.*".toRegex())
        }

        /**
         * Move to destination activity class with animate transition.
         */
        fun moveNextActivity(source: Activity, destinationClass: Class<*>, removeSource: Boolean = false) {
            val intent = Intent(source, destinationClass)

            if (removeSource) {
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            source.startActivity(intent)

            if (removeSource) {
                source.finish()
            }
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        fun checkStoragePermission(context: Context): Boolean {
            val currentAPIVersion = Build.VERSION.SDK_INT
            if (currentAPIVersion >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE)
                return false
            } else {
                return true
            }
        }
    }
}