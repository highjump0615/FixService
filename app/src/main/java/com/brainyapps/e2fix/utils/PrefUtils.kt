package com.brainyapps.e2fix.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

/**
 * Created by Administrator on 3/7/18.
 */
class PrefUtils private constructor(context: Context) {

    private val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun putString(key: String, value: String) {
        prefs.edit().putString(key, value).commit()
    }

    fun putInt(key: String, value: Int) {
        prefs.edit().putInt(key, value).commit()
    }

    fun putBoolean(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).commit()
    }

    fun putLong(key: String, value: Long) {
        prefs.edit().putLong(key, value).commit()
    }

    fun putFloat(key: String, value: Float) {
        prefs.edit().putFloat(key, value).commit()
    }

    fun getString(key: String, defaultValue: String): String? {
        return prefs.getString(key, defaultValue)
    }

    fun getInt(key: String, defaultValue: Int): Int {
        return prefs.getInt(key, defaultValue)
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return prefs.getBoolean(key, defaultValue)
    }

    fun getLong(key: String, defaultValue: Long): Long {
        return prefs.getLong(key, defaultValue)
    }

    fun getFloat(key: String, defaultValue: Float): Float {
        return prefs.getFloat(key, defaultValue)
    }

    companion object {

        val PREF_FILTER_RADIUS = "preference_filter_radius"

        var instance: PrefUtils? = null

        fun init(context: Context) {
            instance = PrefUtils(context)
        }
    }
}
