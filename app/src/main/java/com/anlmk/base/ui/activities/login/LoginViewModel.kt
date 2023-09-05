package com.anlmk.base.ui.activities.login

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.anlmk.base.R
import com.anlmk.base.data.impl.LoginRepo
import com.anlmk.base.data.response.LoginResponse
import com.anlmk.base.di.ResourceProvider
import com.anlmk.base.ml.ModelMalwareDetection
import com.anlmk.base.ui.base.BaseViewModel
import com.anlmk.base.utils.Utils
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder

class LoginViewModel(
    private val loginRepo: LoginRepo,
    val resourcesProvider: ResourceProvider) : BaseViewModel()
{
    val loginResponse = MutableLiveData<LoginResponse>()
    fun login(userName:String,password:String) =
        launch {
            val login = loginRepo.login(userName,password)
            loginResponse.postValue(login)
        }


    fun getuser() =
        launch {
            val response = loginRepo.getUsers()
            Log.d("KHANHAN",response.message())
        }

    fun getStatusApp(isSafeApp: Boolean): String {
        return if (isSafeApp) resourcesProvider.getString(R.string.status_safe_app) else resourcesProvider.getString(R.string.status_unsafe_app)
    }

    fun predictMalware(context: Context,
        modelMalwareDetection: ModelMalwareDetection,
        permissionNames: MutableList<String>
    ): Boolean {
        val byteBuffer = convertPermissionsToByteBuffer(context,permissionNames)
        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 517, 1), DataType.FLOAT32)
        inputFeature0.loadBuffer(byteBuffer)

        val outputs = modelMalwareDetection.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer.floatArray
        outputFeature0.forEachIndexed { _, fl ->
            return fl > 0.5
        }
        return true
    }

    private fun convertPermissionsToByteBuffer(context: Context, permissions: MutableList<String>): ByteBuffer {
        val data = Utils.preprocessingData(permissions, Utils.getLabelFromFile(context,"permission_label.txt"))
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
}