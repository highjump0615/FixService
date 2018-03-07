package com.brainyapps.e2fix.activities

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import com.brainyapps.e2fix.activities.customer.PostJobActivity
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.Permission

/**
 * Created by Administrator on 3/2/18.
 */
class GeoLocationHelper(var ctx: Context, message: String?) {

    private val TAG = PostJobActivity::class.java.getSimpleName()

    var location: Location? = null

    init {
        // 权限设置
        AndPermission.with(ctx)
                .permission(
                        Permission.ACCESS_COARSE_LOCATION,
                        Permission.ACCESS_FINE_LOCATION
                )
                .rationale { context, permissions, executor ->
                    // show confirm dialog
                    AlertDialog.Builder(context)
                            .setTitle("Will you grant location permission?")
                            .setMessage(message)
                            .setPositiveButton(android.R.string.yes, DialogInterface.OnClickListener { dialog, which ->
                                executor.execute()
                            })
                            .setNegativeButton(android.R.string.no, DialogInterface.OnClickListener { dialog, which ->
                                executor.cancel()
                            })
                            .create()
                }
                .onGranted {
                    initLocation()
                }
                .onDenied {
                }
                .start()
    }

    @SuppressLint("MissingPermission")
    private fun initLocation() {
        val serviceName = Context.LOCATION_SERVICE
        val locationManager: LocationManager = ctx.getSystemService(serviceName) as LocationManager

        val bGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val bNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        var lct: Location? = null
        var strProvoider = ""

        if (bGpsEnabled) {
            strProvoider = LocationManager.GPS_PROVIDER
            lct = locationManager.getLastKnownLocation(strProvoider)
        }

        if (lct == null && bNetworkEnabled) {
            strProvoider = LocationManager.NETWORK_PROVIDER
            lct = locationManager.getLastKnownLocation(strProvoider)
        }

        // update new location
        this.location = lct

        if (strProvoider.length > 0) {
            locationManager.requestLocationUpdates(strProvoider, 200000, 100f, mLocationListener)
        }
    }

    private val mLocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            Log.d(TAG, "onLocationChanged");

            this@GeoLocationHelper.location = location
        }

        override fun onProviderDisabled(provider: String) {
            Log.d(TAG, "onProviderDisabled");

            this@GeoLocationHelper.location = null
        }

        override fun onProviderEnabled(provider: String) {
            Log.d(TAG, "onProviderEnabled");
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
            Log.d(TAG, "onStatusChanged provider: $provider, status: $status");
        }
    }
}