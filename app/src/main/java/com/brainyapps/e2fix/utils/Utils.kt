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
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.brainyapps.e2fix.R
import com.firebase.geofire.GeoLocation
import kotlinx.android.synthetic.main.activity_bid_submit.view.*
import kotlinx.android.synthetic.main.layout_dialog_edit.view.*
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

        internal var EARTH_MEAN_RADIUS_MILE = 3958.8

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

        fun createEditDialog(context: Context,
                             title: String,
                             message: String,
                             listener: DialogInterface.OnClickListener? = null): Dialog {
            val builder = AlertDialog.Builder(context)
            val inflater = (context as Activity).layoutInflater
            val view = inflater.inflate(R.layout.layout_dialog_edit, null)

            // placeholder
            view.edit_content.hint = message

            val dialog = builder.setTitle(title)
                    .setView(view)
                    .setPositiveButton(android.R.string.ok, listener)
                    .setNegativeButton(android.R.string.cancel, null)
                    .setCancelable(true)
                    .create()

            return dialog
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

        fun getServerLongTime(): Long {
            val estimatedServerTimeMs = System.currentTimeMillis() + Utils.ServerOffset
            return estimatedServerTimeMs.toLong()
        }

        fun getFormattedDateTime(date: Date): String {
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

        fun getFormattedDate(date: Date): String {
            val dateFormat = SimpleDateFormat("MM/dd, yyyy")
            var result = dateFormat.format(date)

            val period = Utils.getServerTime().time - date.time
            val diffDay = TimeUnit.DAYS.convert(period, TimeUnit.MILLISECONDS)

            if (diffDay == 0L) {
                result = "Today"
            }
            else if (diffDay == 1L) {
                result = "Yesterday"
            }

            return result
        }

        /**
         * compare date only
         */
        fun equalDate(date: Date, dateWith: Date): Boolean {
            val calendar = Calendar.getInstance()
            calendar.time = date

            val calendarWith = Calendar.getInstance()
            calendarWith.time = dateWith

            if (calendar.get(Calendar.DAY_OF_MONTH) == calendarWith.get(Calendar.DAY_OF_MONTH) &&
                calendar.get(Calendar.MONTH) == calendarWith.get(Calendar.MONTH) &&
                calendar.get(Calendar.YEAR) == calendarWith.get(Calendar.YEAR)) {

                return true
            }

            return false
        }

        /**
         * calculate distance between 2 points
         */
        fun distanceInMilesTo(point1: GeoLocation?, point2: GeoLocation?): Double {

            if (point1 == null || point2 == null) {
                return -1.0
            }

            val d2r = 0.017453292519943295
            val lat1rad = point1.latitude * d2r
            val long1rad = point1.longitude * d2r
            val lat2rad = point2.latitude * d2r
            val long2rad = point2.longitude * d2r
            val deltaLat = lat1rad - lat2rad
            val deltaLong = long1rad - long2rad
            val sinDeltaLatDiv2 = Math.sin(deltaLat / 2.0)
            val sinDeltaLongDiv2 = Math.sin(deltaLong / 2.0)
            var a = sinDeltaLatDiv2 * sinDeltaLatDiv2 + Math.cos(lat1rad) * Math.cos(lat2rad) * sinDeltaLongDiv2 * sinDeltaLongDiv2
            a = Math.min(1.0, a)

            val distance = 2.0 * Math.asin(Math.sqrt(a))

            return distance * EARTH_MEAN_RADIUS_MILE
        }

        fun hideKeyboard(context: Context) {
            val view = (context as Activity).getCurrentFocus()
            view?.let {
                val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(it.getWindowToken(), 0)
            }
        }
    }
}