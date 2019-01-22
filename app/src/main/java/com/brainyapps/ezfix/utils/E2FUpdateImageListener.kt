package com.brainyapps.ezfix.utils

import android.app.Activity
import android.view.View

/**
 * Created by Administrator on 2/19/18.
 */

interface E2FUpdateImageListener {
    fun getActivity(): Activity
    fun updatePhotoImageView(byteData: ByteArray)
}
