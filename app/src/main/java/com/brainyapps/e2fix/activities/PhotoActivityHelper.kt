package com.brainyapps.e2fix.activities

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.widget.Toast
import com.brainyapps.e2fix.R
import com.brainyapps.e2fix.utils.E2FUpdateImageListener
import com.brainyapps.e2fix.utils.Utils
import com.cocosw.bottomsheet.BottomSheet
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException

/**
 * Created by Administrator on 2/21/18.
 */
class PhotoActivityHelper(val owner: E2FUpdateImageListener) {

    val REQUEST_CAMERA_CONTENT = 2011
    val REQUEST_IMAGE_CONTENT = 2012
    val REQUEST_IMAGE_CROP = 2014

    var actionSheet: BottomSheet? = null
    var byteData: ByteArray? = null

    var activity: Activity = owner.getActivity()

    init {
        initMenuDialog()
    }

    fun showMenuDialog() {
        this.actionSheet?.show()
    }

    fun hideMenuDialog() {
        this.actionSheet?.dismiss()
    }

    private fun initMenuDialog() {
        this.actionSheet = BottomSheet.Builder(this.activity).title("Profile Photo").sheet(R.menu.photo_menu).listener(DialogInterface.OnClickListener { dialog, which ->
            when (which) {
                R.id.profile_menu_camera -> {
                    if (ActivityCompat.checkSelfPermission(this.activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        Utils.checkStoragePermission(this.activity)
                        return@OnClickListener
                    }

                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    this.activity.startActivityForResult(intent, REQUEST_CAMERA_CONTENT)
                }
                R.id.profile_menu_photo -> {
                    if (ActivityCompat.checkSelfPermission(this.activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        Utils.checkStoragePermission(this.activity)
                        return@OnClickListener
                    }

                    val pictureActionIntent = Intent()
                    pictureActionIntent.type = "image/*"
                    pictureActionIntent.action = Intent.ACTION_PICK
                    pictureActionIntent.putExtra("crop", true)
                    pictureActionIntent.putExtra("aspectX", 0)
                    pictureActionIntent.putExtra("aspectY", 0)
                    pictureActionIntent.putExtra("outputX", 300)
                    pictureActionIntent.putExtra("outputY", 300)
                    pictureActionIntent.putExtra("return-data", true)
                    this.activity.startActivityForResult(Intent.createChooser(pictureActionIntent,
                            "Complete action using"), REQUEST_IMAGE_CONTENT)
                }
                R.id.profile_menu_cancel -> this.actionSheet!!.dismiss()
                else -> {
                }
            }
            hideMenuDialog()
        }).build()
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        when (requestCode) {
            REQUEST_CAMERA_CONTENT -> if (resultCode == Activity.RESULT_OK) {
                onCaptureImageResult(data!!)
            }
            REQUEST_IMAGE_CROP, REQUEST_IMAGE_CONTENT -> if (resultCode == Activity.RESULT_OK) {
                var selectedBitmap: Bitmap? = null

                if (data!!.data == null) {
                    val extras = data.extras
                    if (extras != null) {
                        selectedBitmap = extras.getParcelable("data")
                    }
                } else {
                    selectedBitmap = rotatePortrait(getPath(data.data))
                }

                if (selectedBitmap != null) {
                    val bytes = ByteArrayOutputStream()
                    selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                    byteData = bytes.toByteArray()

                    owner.updatePhotoImageView(byteData!!)
                }
            }
        }
    }

    private fun onCaptureImageResult(data: Intent) {
        val uri = data.data

        try {
            //Start Crop Activity

            val cropIntent = Intent("com.android.camera.action.CROP")

            cropIntent.setDataAndType(uri, "image/*")
            // set crop properties
            cropIntent.putExtra("crop", true)
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 0)
            cropIntent.putExtra("aspectY", 0)
            // indicate output X and Y
            cropIntent.putExtra("outputX", 300)
            cropIntent.putExtra("outputY", 300)

            // retrieve data on return
            cropIntent.putExtra("return-data", true)
            // start the activity - we handle returning in onActivityResult
            this.activity.startActivityForResult(cropIntent, REQUEST_IMAGE_CROP)
        } catch (anfe: ActivityNotFoundException) {
            // display an error message
            val errorMessage = "your device doesn't support the crop action!"
            val thumbnail = data.extras!!.get("data") as Bitmap

            val bytes = ByteArrayOutputStream()
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            byteData = bytes.toByteArray()

            owner.updatePhotoImageView(byteData!!)
        }
    }

    fun rotatePortrait(filepath: String): Bitmap? {
        val file = File(filepath)
        var cropped: Bitmap? = null
        if (file.exists()) {
            val matrix = Matrix()
            var exif: ExifInterface? = null
            var rotateangle = 0

            try {
                exif = ExifInterface(file.absolutePath)
                val orientation = exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL)

                if (orientation == ExifInterface.ORIENTATION_ROTATE_90)
                    rotateangle = 90
                else if (orientation == ExifInterface.ORIENTATION_ROTATE_180)
                    rotateangle = 180
                else if (orientation == ExifInterface.ORIENTATION_ROTATE_270)
                    rotateangle = 270

            } catch (e: IOException) {
                e.printStackTrace()
            }

            matrix.postRotate(rotateangle.toFloat())

            val source = BitmapFactory.decodeFile(filepath)

            if (source != null) {
                cropped = Bitmap.createBitmap(source, 0, 0,
                        source.width, source.height, matrix, true)
            } else {
                Toast.makeText(this.activity, "Failed to load image", Toast.LENGTH_SHORT).show()
            }
        }

        return cropped
    }

    fun getPath(uri: Uri?): String {
        var cursor: Cursor? = null
        try {
            val proj = arrayOf(MediaStore.Video.Media.DATA)
            cursor = this.activity.getContentResolver().query(uri!!, proj, null, null, null)
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(column_index)
        } finally {
            if (cursor != null) {
                cursor.close()
            }
        }
    }
}