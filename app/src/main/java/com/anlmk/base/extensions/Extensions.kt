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
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.os.SystemClock
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import com.anlmk.base.BuildConfig
import com.anlmk.base.R
import com.anlmk.base.data.impl.Mealtime
import com.anlmk.base.ui.views.TextView
import com.anlmk.base.utils.RequestCode
import com.anlmk.base.utils.Utils
import java.io.*
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
        Log.wtf("Exception",e.message)
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
        Log.wtf("Exception",e.message)
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

fun Context.saveBitmapToInternalStorage(bitmap: Bitmap, filename: String): String {
    val fileOutputStream: FileOutputStream
    try {
        fileOutputStream = this.openFileOutput(filename, Context.MODE_PRIVATE)
        bitmap.getResizedBitmap()?.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
        fileOutputStream.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return filename
}

fun Context.loadBitmapFromInternalStorage(filename: String): Bitmap? {
    return try {
        val fileInputStream = this.openFileInput(filename)
        BitmapFactory.decodeStream(fileInputStream)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun Context.deleteFileFromInternalStorage(filename: String): Boolean {
    val file = getFileStreamPath(filename)
    return if (file.exists()) {
        file.delete()
    } else {
        false
    }
}

fun Bitmap.getResizedBitmap(): Bitmap? {
    val width = this.width
    val height = this.height
    val wd = 500

    return if (width < wd) {
        this
    } else {
        val scaleWidth = wd.toFloat() / width.toFloat()
        val scaleHeight = scaleWidth * height.toFloat() / width.toFloat()
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        Bitmap.createBitmap(this, 0, 0, width, height, matrix, false)
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
fun Context.exportMealtimeListToCSVUseMediaStore(mealtimeList: List<Mealtime?>):Boolean {
    val fileName = this.getString(R.string.app_name)

    val contentValues = ContentValues().apply {
        put(MediaStore.Downloads.DISPLAY_NAME, fileName)
        put(MediaStore.Downloads.MIME_TYPE, "text/csv")
        put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
    }

    val contentUri = MediaStore.Downloads.EXTERNAL_CONTENT_URI
    val resolver = this.contentResolver
    val uri = resolver.insert(contentUri, contentValues)

    uri?.let { outputFileUri ->
        try {
            val outputStream: OutputStream? = resolver.openOutputStream(outputFileUri)
            outputStream?.let {
                val outputStreamWriter = OutputStreamWriter(it)
                val header = "index,Buổi,Thứ,Ngày,Giờ,Thức ăn gồm,Sau 2 giờ ăn\n"
                outputStreamWriter.write(header)
                for ((index, mealtime) in mealtimeList.withIndex()) {
                    val dateOfMeal = Utils.getDateFormat().format(mealtime?.dateOfMeal)
                    val timeOfMeal = Utils.getTimeFormat().format(mealtime?.timeOfMeal)
                    val mmol = this.getString(R.string.value_mmol,mealtime?.molOfFood)
                    val food = "\"${mealtime?.foodOfMeal}\""
                    val line = "${index+1},${mealtime?.sessionName},${dateOfMeal},${timeOfMeal},${food},${mmol}\n"
                    outputStreamWriter.write(line)
                }

                outputStreamWriter.close()
                return true
                // Show a toast or a message indicating success
                // Toast.makeText(context, "Mealtime data exported", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()

            // Handle the error, show a toast or a message indicating failure
            // Toast.makeText(context, "Export failed", Toast.LENGTH_SHORT).show()
        }
    }
    return false
}
fun Context.exportMealtimeListToCSV(mealtimeList: List<Mealtime?>):Boolean {
    val fileName = this.getString(R.string.app_name)
    val folder = File(Environment.getExternalStorageDirectory(), "MyAppFolder")

    if (!folder.exists()) {
        folder.mkdirs()
    }

    val file = File(folder, fileName)

    try {
        val fileWriter = FileWriter(file)

        // Write the CSV header
        val header = "index,Buổi,Thứ,Ngày,Giờ,Thức ăn gồm,Sau 2 giờ ăn\n"

        fileWriter.append(header)
        fileWriter.append("\n")

        for ((index, mealtime) in mealtimeList.withIndex()) {
            val dateOfMeal = Utils.getDateFormat().format(mealtime?.dateOfMeal)
            val timeOfMeal = Utils.getTimeFormat().format(mealtime?.timeOfMeal)
            val mmol = this.getString(R.string.value_mmol,mealtime?.molOfFood)
            val food = "\"${mealtime?.foodOfMeal}\""
            val line = "${index+1},${mealtime?.sessionName},${dateOfMeal},${timeOfMeal},${food},${mmol}"
            fileWriter.append(line)
            fileWriter.append("\n")
        }

        fileWriter.flush()
        fileWriter.close()
        return true
        // Show a toast or a message indicating success
        // Toast.makeText(context, "Mealtime data exported to $file", Toast.LENGTH_SHORT).show()

    } catch (e: Exception) {
        e.printStackTrace()

        // Handle the error, show a toast or a message indicating failure
        // Toast.makeText(context, "Export failed", Toast.LENGTH_SHORT).show()
    }
    return false
}







