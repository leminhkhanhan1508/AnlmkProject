package com.anlmk.base.data.`object`

import android.graphics.drawable.Drawable

data class InstalledApplicationInfo(
    var packageName: String? = null,
    var appName: String? = null,
    var iconApp: Drawable? = null,
    var isSafeApp: Boolean = true,
    var statusApp: String? = null,
    var listPermission: ArrayList<String>? = null
)
