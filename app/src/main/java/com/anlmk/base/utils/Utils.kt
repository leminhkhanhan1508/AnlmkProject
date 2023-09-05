package com.anlmk.base.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import com.anlmk.base.data.`object`.InstalledApplicationInfo
import com.anlmk.base.ml.ModelMalwareDetection
import com.google.gson.Gson
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader


object Utils {
    fun getLabelFromFile(context: Context, fileName: String): List<String> {
        val stringBuilder = StringBuilder()
        try {
            // Mở một luồng đọc tệp tin từ thư mục 'assets'
            val inputStream: InputStream = context.getAssets().open(fileName)

            // Đọc dữ liệu từ luồng đầu vào
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }

            // Đóng luồng và trả về nội dung đọc được
            inputStream.close()
            return stringBuilder.toString().trim().uppercase().split(",")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return arrayListOf()
    }

    fun preprocessingData(listPermission: List<String>, listLabel: List<String>): List<Int> {
        val listResult = IntArray(listLabel.size) { 0 }
        for (permission in listPermission) {
            if (permission.trim().uppercase() in listLabel) {
                listResult[listLabel.indexOf(permission.trim().uppercase())] = 1
            }
        }
        return listResult.toList()
    }

    fun getInstalledApplication(context: Context): List<InstalledApplicationInfo> {
        val pm = context.packageManager
        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)

        val resolvedInfos = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            pm.queryIntentActivities(
                mainIntent,
                PackageManager.ResolveInfoFlags.of(0L)
            )
        } else {
            pm.queryIntentActivities(mainIntent, 0)
        }

        var listInstalledApplicationInfo: ArrayList<InstalledApplicationInfo> = arrayListOf()
        for (item in resolvedInfos){
            val appName = item.activityInfo.applicationInfo.loadLabel(pm).toString()
            val packageName = item.activityInfo.packageName
            val iconDrawable = item.activityInfo.loadIcon(pm)
            val requestedPermissions = pm.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS).requestedPermissions
            val permissionNames: ArrayList<String> = ArrayList()
            if (requestedPermissions==null){
                Log.wtf("KHANHANDEBUG",appName+packageName)
                break
            }
            for (permission in requestedPermissions) {
                try {
                    val permissionInfo = pm.getPermissionInfo(permission!!, 0)
                    permissionNames.add(permissionInfo.name)
                } catch (e: PackageManager.NameNotFoundException) {
                    e.printStackTrace()
                }
            }
            listInstalledApplicationInfo.add(InstalledApplicationInfo().apply {
                this.packageName = packageName
                this.appName = appName
                this.iconApp = iconDrawable
                this.listPermission = permissionNames
            })
        }
        return listInstalledApplicationInfo

    }


}