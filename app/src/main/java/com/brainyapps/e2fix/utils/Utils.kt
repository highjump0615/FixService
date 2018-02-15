package com.brainyapps.e2fix.utils

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
    }
}