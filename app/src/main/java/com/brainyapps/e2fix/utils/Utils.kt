package com.brainyapps.e2fix.utils

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.text.TextUtils
import com.google.firebase.iid.FirebaseInstanceId
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by Administrator on 2/15/18.
 */
class Utils {

    companion object {

        val PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 9009
        var progressDlg: ProgressDialog? = null

        var ServerOffset = 0.0

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
        fun moveNextActivity(source: Activity, destinationClass: Class<*>, removeSource: Boolean = false, removeAll: Boolean = false) {
            val intent = Intent(source, destinationClass)

            if (removeAll) {
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

        fun checkConnection(context: Context): Boolean {
            val networkStatus = isNetworkAvailable(context)
            if (!networkStatus) {
                createErrorAlertDialog(context, "Warning",
                        "Oops! Please connect to the internet and try again.")
            }
            return networkStatus
        }

        fun isNetworkAvailable(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }

        /**
         * Create error AlertDialog.
         */
        fun createErrorAlertDialog(context: Context, title: String, message: String, listener: DialogInterface.OnClickListener? = null): Dialog {
            return AlertDialog.Builder(context)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok, listener).create()
        }

        fun createProgressDialog(context: Context, title:String, message: String): ProgressDialog? {
            progressDlg = ProgressDialog.show(context, title, message);

            return progressDlg
        }

        fun closeProgressDialog() {
            progressDlg?.dismiss()
        }

        fun getServerTime(): Date {
            val estimatedServerTimeMs = System.currentTimeMillis() + Utils.ServerOffset
            return Date(estimatedServerTimeMs.toLong())
        }

        fun getFormattedDate(date: Date): String {
            val period = Utils.getServerTime().time - date.time
            val value = TimeUnit.MINUTES.convert(period, TimeUnit.MILLISECONDS)
            if (value == 0L) {
                return "0 min ago"
            } else if (value < 60) {
                return value.toString() + " mins ago"
            } else if (value == 60L) {
                return "1 hour ago"
            } else if (value < 120) {
                return "1 hour " + (value - 60) + " mins ago"
            } else if (value < 720) {
                return "" + (value / 60).toString() + " hours ago"
            } else if (value < 1440) {
                return "Today " + SimpleDateFormat("HH:mm").format(date)
            } else if (value < 2880) {
                return "Yesterday " + SimpleDateFormat("HH:mm").format(date)
            }

            return SimpleDateFormat("MM/dd, yyyy").format(date)
        }
    }
}