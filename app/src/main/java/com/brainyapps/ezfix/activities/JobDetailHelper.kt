package com.brainyapps.ezfix.activities

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.brainyapps.ezfix.R
import com.brainyapps.ezfix.models.Job
import com.brainyapps.ezfix.utils.Utils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.util.*

/**
 * Created by Administrator on 3/2/18.
 */
class JobDetailHelper(val contentView: View) {

    companion object {
        val KEY_JOB = "job"
    }

    init {
    }

    fun fillJobInfo(job: Job) {

        // photo
        val imgview = contentView.findViewById<ImageView>(R.id.imgview_photo)
        Glide.with(contentView.context)
                .load(job.photoUrl)
                .apply(RequestOptions.placeholderOf(R.drawable.job_default).fitCenter())
                .into(imgview)

        // title
        var textview = contentView.findViewById<TextView>(R.id.text_job_title)
        textview.text = job.title

        // time
        textview = contentView.findViewById<TextView>(R.id.text_time)
        textview.text = Utils.getFormattedDateTime(Date(job.createdAt))

        // description
        textview = contentView.findViewById<TextView>(R.id.text_job_desc)
        textview.text = job.description

        // location
        textview = contentView.findViewById<TextView>(R.id.text_job_location)
        textview.text = job.location

        // bid
        textview = contentView.findViewById<TextView>(R.id.text_job_bid)
        textview?.text = "Bids: ${job.bidArray.count()}"
    }
}