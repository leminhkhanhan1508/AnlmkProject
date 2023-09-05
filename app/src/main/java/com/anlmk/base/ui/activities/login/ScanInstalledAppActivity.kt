package com.anlmk.base.ui.activities.login

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.anlmk.base.R
import com.anlmk.base.databinding.ActivityScanInstalledAppBinding
import com.anlmk.base.ml.ModelMalwareDetection
import com.anlmk.base.ui.adapters.CommonAdapter
import com.anlmk.base.ui.base.BaseActivity
import com.anlmk.base.utils.Utils
import org.koin.android.viewmodel.ext.android.viewModel
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder


class ScanInstalledAppActivity : BaseActivity() {
    override val model: LoginViewModel by viewModel()
    override val binding by lazy {
        ActivityScanInstalledAppBinding.inflate(layoutInflater)
    }
    private var adapterServiceHome: CommonAdapter? = null

    lateinit var modelMalwareDetection: ModelMalwareDetection
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setAdapterServiceHome()
        modelMalwareDetection = ModelMalwareDetection.newInstance(this)
        val listInstalledApplicationInfo = Utils.getInstalledApplication(this)
        listInstalledApplicationInfo.forEach {
            val isSafeApp = model.predictMalware(
                this,
                modelMalwareDetection,
                it.listPermission ?: arrayListOf()
            )
            it.isSafeApp = isSafeApp
            it.statusApp = model.getStatusApp(isSafeApp)
        }
        adapterServiceHome?.updateData(listInstalledApplicationInfo)
        modelMalwareDetection.close()
    }



    private fun setAdapterServiceHome() {
        adapterServiceHome = CommonAdapter()
        val layoutManagerNormal =  LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rcvHomeServiceFunction.layoutManager = layoutManagerNormal
        binding.rcvHomeServiceFunction.adapter = adapterServiceHome
    }

}