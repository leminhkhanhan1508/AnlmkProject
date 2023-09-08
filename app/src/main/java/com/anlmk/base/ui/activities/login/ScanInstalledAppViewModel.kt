package com.anlmk.base.ui.activities.login

import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import com.anlmk.base.R
import com.anlmk.base.data.impl.ScanInstalledAppRepo
import com.anlmk.base.data.`object`.InstalledApplicationInfo
import com.anlmk.base.di.ResourceProvider
import com.anlmk.base.ml.ModelMalwareDetection
import com.anlmk.base.ui.activities.home.HomeActivity
import com.anlmk.base.ui.base.BaseViewModel
import com.anlmk.base.utils.InstallerInformation
import com.anlmk.base.utils.Tags
import com.anlmk.base.utils.Utils
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder

class ScanInstalledAppViewModel(
    private val scanInstalledAppRepo: ScanInstalledAppRepo,
    val resourcesProvider: ResourceProvider
) : BaseViewModel() {
    var percentLoading = MutableLiveData<Float>()
    var codeFunction: String? = null
    fun getStatusApp(isSafeApp: Boolean): String {
        return if (isSafeApp) resourcesProvider.getString(R.string.status_safe_app) else resourcesProvider.getString(
            R.string.status_unsafe_app
        )
    }

    fun predictMalware(
        modelMalwareDetection: ModelMalwareDetection,
        permissionNames: MutableList<String>,
        listLabel: List<String>
    ): Float {
        val byteBuffer = convertPermissionsToByteBuffer(permissionNames,listLabel)
        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 517, 1), DataType.FLOAT32)
        inputFeature0.loadBuffer(byteBuffer)

        val outputs = modelMalwareDetection.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer.floatArray
        outputFeature0.forEachIndexed { _, fl ->
            return fl
        }
        return 0F
    }

    private fun convertPermissionsToByteBuffer(
        permissions: MutableList<String>,
        listLabel: List<String>
    ): ByteBuffer {
        val data = Utils.preprocessingData(
            permissions,listLabel)
        val inputSize = data.size
        // Tạo ByteBuffer với kích thước tương ứng
        val byteBuffer = ByteBuffer.allocateDirect(inputSize * 4)  // 4 bytes cho kiểu FLOAT32
        byteBuffer.order(ByteOrder.nativeOrder())  // Đảm bảo đúng thứ tự byte
        for (i in 0 until inputSize) {
            val inputValue = data[i].toFloat()
            byteBuffer.putFloat(inputValue)
        }
        byteBuffer.rewind()  // Đặt con trỏ về vị trí đầu
        return byteBuffer
    }

    fun getInstalledApplication(
        context: Context,
        modelMalwareDetection: ModelMalwareDetection,
        codeFunction: String
    ): List<InstalledApplicationInfo> {
        val pm = context.packageManager
        val installedApplications =
            pm.getInstalledApplications(0).filter { it.flags and ApplicationInfo.FLAG_SYSTEM == 0 }

        var listInstalledApplicationInfo: ArrayList<InstalledApplicationInfo> = arrayListOf()
        var count = 0F
        val listLabel = Utils.getLabelFromFile(context, "permission_label.txt")
        for (applicationInfo in installedApplications) {
            count += 1
            Handler(Looper.getMainLooper()).post {
                percentLoading.value = ((count * 100 / installedApplications.size))
            }

            // This is a user-installed application, not a system app
            val packageName = applicationInfo.packageName ?: ""
            val listPermission = Utils.getPermissionNames(packageName, pm)
            val percentageOfMalware = predictMalware(
                modelMalwareDetection,
                listPermission,
                listLabel
            )
            val isSafeApp = percentageOfMalware < 0.5
            listInstalledApplicationInfo.add(InstalledApplicationInfo().apply {
                this.packageName = packageName
                this.appName = pm.getApplicationLabel(applicationInfo).toString()
                this.iconApp = applicationInfo.loadIcon(pm)
                this.installerInfo = Utils.getInstallerInfo(packageName, pm)
                this.listPermission = listPermission
                this.percentageOfMalware = percentageOfMalware
                this.isSafeApp = isSafeApp
                this.statusApp = getStatusApp(isSafeApp)
            })

        }
        return filterInstallApplications(codeFunction,listInstalledApplicationInfo)
    }

    private fun filterInstallApplications(
        codeFunction: String,
        listInstalledApplicationInfo: ArrayList<InstalledApplicationInfo>
    ): List<InstalledApplicationInfo> {
        when (codeFunction) {
            HomeActivity.HOME_SAFE_APP -> {
                return listInstalledApplicationInfo.filter { it.isSafeApp }
            }
            HomeActivity.HOME_UNSAFE_APP -> {
                return listInstalledApplicationInfo.filter { !it.isSafeApp }
            }
            HomeActivity.HOME_UNKNOWN_APP -> {
                return listInstalledApplicationInfo.filter { it.installerInfo != InstallerInformation.CH_PLAY }
            }
        }
        return listInstalledApplicationInfo
    }
}