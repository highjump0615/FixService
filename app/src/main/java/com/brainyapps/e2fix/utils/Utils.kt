package com.brainyapps.e2fix.utils

import android.app.Activity
import android.content.Intent
import android.text.TextUtils

/**
 * Created by Administrator on 2/15/18.
 */
class Utils {

    companion object {

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
        fun moveNextActivity(source: Activity, destinationClass: Class<*>, removeSource: Boolean) {
            val intent = Intent(source, destinationClass)

            if (removeSource) {
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            source.startActivity(intent)

            if (removeSource) {
                source.finish()
            }
        }
    }
}