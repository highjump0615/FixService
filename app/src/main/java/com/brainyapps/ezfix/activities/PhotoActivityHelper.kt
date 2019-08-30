package com.brainyapps.ezfix.activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.FileProvider
import android.widget.Toast
import com.brainyapps.ezfix.R
import com.brainyapps.ezfix.utils.E2FUpdateImageListener
import com.brainyapps.ezfix.utils.Utils
import com.cocosw.bottomsheet.BottomSheet
import com.theartofdev.edmodo.cropper.CropImage
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.Permission
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

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

    private var mCurrentPhotoPath: String = ""

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
                        Utils.checkPermission(this.activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        return@OnClickListener
                    }

                    Utils.checkPermission(this.activity, Manifest.permission.CAMERA)

                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    this.activity.startActivityForResult(intent, REQUEST_CAMERA_CONTENT)
                }
                R.id.profile_menu_photo -> {
                    if (ActivityCompat.checkSelfPermission(this.activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        Utils.checkPermission(this.activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
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

    private fun createImageFile(): File? {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmssSS").format(Date())
        val imageFileName = "JPEG_${timeStamp}_"
        val storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(imageFileName, ".jpg", storageDir)

        mCurrentPhotoPath = image.absolutePath

        return image
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        when (requestCode) {
            REQUEST_CAMERA_CONTENT -> if (resultCode == Activity.RESULT_OK) {
                onCaptureImageResult(data!!)
            }
            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                val result = CropImage.getActivityResult(data)

                if (resultCode == Activity.RESULT_OK) {
                    val resultUri = result.uri
                    handleImage(rotatePortrait(resultUri.path))
                }
                else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    val error = result.error
                }
            }
            REQUEST_IMAGE_CONTENT -> if (resultCode == Activity.RESULT_OK) {
                var selectedBitmap: Bitmap? = null

                if (data!!.data == null) {
                    val extras = data.extras
                    if (extras != null) {
                        selectedBitmap = extras.getParcelable("data")
                    }
                } else {
                    selectedBitmap = rotatePortrait(getPath(data.data))
                }

                handleImage(selectedBitmap)
            }
        }
    }

    private fun onCaptureImageResult(data: Intent?) {
        if (data == null) {
            val file = File(mCurrentPhotoPath)
            if (file.exists()) {
                CropImage.activity(Uri.fromFile(file)).start(activity);
                return
            }
        }

        var uri = data?.data
        if (uri == null) {
            val thumbnail = data?.extras?.get("data")

            if (thumbnail != null) {
                uri = getImageUri(activity.applicationContext, thumbnail as Bitmap)
                beginCrop(uri)
            }
        }
        else {
            beginCrop(uri)
        }
    }

    private fun beginCrop(source: Uri) {
        // start cropping activity for pre-acquired image saved on the device
        CropImage.activity(source).start(activity);
    }

    private fun handleImage(bitmap: Bitmap?) {
        bitmap?.let {
            val bytes = ByteArrayOutputStream()
            it.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            byteData = bytes.toByteArray()

            owner.updatePhotoImageView(byteData!!)
        }
    }

    private fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 90, bytes)

        val path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "temp_file", null)
        return Uri.parse(path)
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