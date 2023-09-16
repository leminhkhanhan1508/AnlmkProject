package com.anlmk.base.extensions

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.os.SystemClock
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import com.anlmk.base.BuildConfig
import com.anlmk.base.R
import com.anlmk.base.ui.views.TextView
import com.anlmk.base.utils.RequestCode
import com.anlmk.base.utils.Utils
import java.io.File
import java.io.FileDescriptor
import java.io.InputStream
import java.util.*


fun Exception.safeLog() {
    if (BuildConfig.DEBUG) printStackTrace()
}

fun Throwable.safeLog() {
    if (BuildConfig.DEBUG) printStackTrace()
}

fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener {
        try {
            onSafeClick(it)
        } catch (e: java.lang.Exception) {
            Log.wtf("EX", e)
        }
    }
    setOnClickListener(safeClickListener)
}

fun View.remove() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}

fun TextView.clear() {
    text = ""
}

class SafeClickListener(
    private var defaultInterval: Int = 1000,
    private val onSafeCLick: (View) -> Unit
) : View.OnClickListener {
    private var lastTimeClicked: Long = 0
    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - lastTimeClicked < defaultInterval) {
            return
        }
        lastTimeClicked = SystemClock.elapsedRealtime()
        onSafeCLick(v)
    }
}


fun TextView.showDialogDate() {
    val date = Utils.getDateFormat().parse(this.text.toString())
    val currentCalendar = Calendar.getInstance()
    if (date != null) {
        currentCalendar.time = date
    }
    val year = currentCalendar.get(Calendar.YEAR)
    val month = currentCalendar.get(Calendar.MONTH)
    val day = currentCalendar.get(Calendar.DATE)


    val dialog = DatePickerDialog(
        this.context,
        { _, selectedYear, selectedMonth, selectedDay ->
            var year12 = selectedYear
            var month12 = selectedMonth
            var day12 = selectedDay

            val lc = Calendar.getInstance()
            lc.set(Calendar.YEAR, year12)
            lc.set(Calendar.MONTH, month12)
            lc.set(Calendar.DAY_OF_MONTH, day12)
            this.text = Utils.getDateFormat().format(lc.time)
        },
        year,
        month,
        day
    )

    dialog.show()
    dialog.getButton(DatePickerDialog.BUTTON_NEGATIVE)
        .setTextColor(ContextCompat.getColor(this.context, R.color.color_00a1e4))
    dialog.getButton(DatePickerDialog.BUTTON_POSITIVE)
        .setTextColor(ContextCompat.getColor(this.context, R.color.color_00a1e4))
}

fun TextView.showDialogTimePicker() {
    val time = Utils.getTimeFormat().parse(this.text.toString())
    val currentCalendar = Calendar.getInstance()
    if (time != null) {
        currentCalendar.time = time
    }
    val hour = currentCalendar.get(Calendar.HOUR)
    val minute = currentCalendar.get(Calendar.MINUTE)


    val dialog = TimePickerDialog(
        this.context,
        { _, selectedHour, selectedMinute ->
            val lc = Calendar.getInstance()
            lc.set(Calendar.HOUR, selectedHour)
            lc.set(Calendar.MINUTE, selectedMinute)
            this.text = Utils.getTimeFormat().format(lc.time)
        },
        hour,
        minute,
        false
    )

    dialog.show()
    dialog.getButton(DatePickerDialog.BUTTON_NEGATIVE)
        .setTextColor(ContextCompat.getColor(this.context, R.color.color_00a1e4))
    dialog.getButton(DatePickerDialog.BUTTON_POSITIVE)
        .setTextColor(ContextCompat.getColor(this.context, R.color.color_00a1e4))
}


fun Date.formatDateDefaults(): String {
    return Utils.getDateFormat().format(this)
}

fun Date.formatTimeDefaults(): String {
    return Utils.getTimeFormat().format(this)
}


fun Context.getScreenWidth(): Int {
    val displays = (this.applicationContext.getSystemService(
        Context.WINDOW_SERVICE
    ) as WindowManager)
        .defaultDisplay
    return displays.width
}


fun Activity.openCamera():Uri? {
    try {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val imageFileName = "takePhoto.jpg"
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, imageFileName)
        val outputUri = this.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            values
        )
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri)
        this.startActivityForResult(
            cameraIntent,
            RequestCode.IMAGE_CAPTURE_CODE
        )
        return outputUri
    } catch (e: Exception) {
        Log.wtf("KHANHANDEBUG",e.message)
    }
    return null
}

fun FragmentActivity.requestPermission(
    permissions: Array<String>,
    requestCode: Int,
    onAction: (() -> Unit)? = null,
) {
    val permissionsToRequest = mutableListOf<String>()
    permissions.forEach { permission ->
        if (ActivityCompat.checkSelfPermission(
                this,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionsToRequest.add(permission)
        }
    }
    if (permissionsToRequest.isNotEmpty()) {
        ActivityCompat.requestPermissions(this, permissionsToRequest.toTypedArray(), requestCode)
    } else {
        onAction?.invoke()
    }
}

fun Activity.openFileSystemManager() {
    try {
        var intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        var mimetypes = arrayOf("image/*")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes)
        startActivityForResult(intent, RequestCode.REQUEST_GALLERY)
    } catch (e: java.lang.Exception) {
        Log.wtf("KHANHANDEBUG",e.message)
    }
}

fun Context.getBitmapFromUri(uri: Uri): Bitmap? {
    return try {
        val parcelFileDescriptor: ParcelFileDescriptor? = this.contentResolver.openFileDescriptor(uri, "r")
        val fileDescriptor: FileDescriptor? = parcelFileDescriptor?.fileDescriptor
        val image: Bitmap? = BitmapFactory.decodeFileDescriptor(fileDescriptor)
        parcelFileDescriptor?.close()
        image
    } catch (var6: Exception) {
        null
    }
}

