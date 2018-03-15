package com.brainyapps.e2fix.activities

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.models.Job
import com.brainyapps.e2fix.models.Report
import com.brainyapps.e2fix.models.User
import com.brainyapps.e2fix.utils.Utils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.util.*

/**
 * Created by Administrator on 3/2/18.
 */
class ReportHelper(val owner: Activity) {

    init {
    }

    fun addReport(userReportedId: String) {
        Utils.createEditDialog(owner,
                "Report Post",
                "Text content about report",
                DialogInterface.OnClickListener { dialog, which ->
                    val dlg = dialog as AlertDialog
                    val editContent = dlg.findViewById<EditText>(R.id.edit_content)

                    // add report
                    val newReport = Report()
                    newReport.userId = User.currentUser!!.id
                    newReport.user = User.currentUser
                    newReport.userReportedId = userReportedId
                    newReport.content = editContent.text.toString()

                    newReport.saveToDatabase()

                    Toast.makeText(owner, "Reported successfully", Toast.LENGTH_SHORT).show()
                })
                .show()
    }
}