package com.anlmk.base.extensions

import android.os.SystemClock
import android.util.Log
import android.view.View
import com.anlmk.base.BuildConfig
import com.anlmk.base.ui.views.TextView


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
