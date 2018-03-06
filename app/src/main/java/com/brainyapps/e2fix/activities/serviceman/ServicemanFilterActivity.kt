package com.brainyapps.e2fix.activities.serviceman

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.activities.BaseActivity
import kotlinx.android.synthetic.main.activity_serviceman_filter.*
import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar

class ServicemanFilterActivity : BaseActivity(), DiscreteSeekBar.OnProgressChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_serviceman_filter)

        setNavbar(null, true)

        this.seekbar.setOnProgressChangeListener(this)
    }

    override fun onProgressChanged(seekBar: DiscreteSeekBar?, value: Int, fromUser: Boolean) {
        this.text_filter_distance.text = "$value miles"
    }

    override fun onStartTrackingTouch(seekBar: DiscreteSeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: DiscreteSeekBar?) {
    }

}
