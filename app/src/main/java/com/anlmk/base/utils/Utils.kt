package com.anlmk.base.utils

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
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



    fun getPermissionNames(packageName: String, pm: PackageManager): ArrayList<String> {
        val permissionNames: ArrayList<String> = ArrayList()

        if (packageName.isEmpty()) {
            return permissionNames
        }

        try {
            val packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS)
            val requestedPermissions = packageInfo.requestedPermissions
            if (requestedPermissions != null){
                permissionNames.addAll(requestedPermissions)
            }else{
                Log.wtf("KHANHANDEBUG-getPermissionNames",packageName)
            }
//            if (requestedPermissions != null) {
//                for (permission in requestedPermissions) {
//                    try {
//                        val permissionInfo = pm.getPermissionInfo(permission, 0)
//                        permissionNames.add(permissionInfo.name)
//                    } catch (e: PackageManager.NameNotFoundException) {
//                        Log.wtf("KHANHANDEBUG",packageName+"-"+e.message)
//                        e.printStackTrace()
//                    }
//                }
//            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return permissionNames
    }

    fun getInstallerInfo(packageName: String, packageManager: PackageManager): String? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            packageManager.getInstallSourceInfo(packageName).installingPackageName
        } else {
            packageManager.getInstallerPackageName(packageName)
        }
    }

}