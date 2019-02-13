package com.brainyapps.ezfix.activities.serviceman

import android.os.Bundle
import com.brainyapps.ezfix.R
import com.brainyapps.ezfix.activities.BaseActivity
import com.brainyapps.ezfix.utils.PrefUtils
import kotlinx.android.synthetic.main.activity_serviceman_filter.*
import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar

class ServicemanFilterActivity : BaseActivity(), DiscreteSeekBar.OnProgressChangeListener {

    var mRadius: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_serviceman_filter)

        setNavbar(null, true)

        // load radius from shared preference
        mRadius = PrefUtils.instance!!.getInt(PrefUtils.PREF_FILTER_RADIUS, 0)

        this.seekbar.progress = mRadius
        this.seekbar.setOnProgressChangeListener(this)

        updateText()
    }

    override fun onProgressChanged(seekBar: DiscreteSeekBar?, value: Int, fromUser: Boolean) {
        mRadius = value
        updateText()
    }

    override fun onStartTrackingTouch(seekBar: DiscreteSeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: DiscreteSeekBar?) {
    }

    fun updateText() {
        this.text_filter_distance.text = "$mRadius miles"
    }

    override fun onBackPressed() {
        super.onBackPressed()

        // save radius to shared preference
        PrefUtils.instance!!.putInt(PrefUtils.PREF_FILTER_RADIUS, mRadius)
    }
}
