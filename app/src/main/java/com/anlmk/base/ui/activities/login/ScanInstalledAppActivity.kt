package com.anlmk.base.ui.activities.login

import android.os.*
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.anlmk.base.R
import com.anlmk.base.data.`object`.InstalledApplicationInfo
import com.anlmk.base.databinding.ActivityScanInstalledAppBinding
import com.anlmk.base.extensions.remove
import com.anlmk.base.extensions.show
import com.anlmk.base.ml.ModelMalwareDetection
import com.anlmk.base.ui.adapters.CommonAdapter
import com.anlmk.base.ui.base.BaseActivity
import com.anlmk.base.utils.Tags
import com.anlmk.base.utils.Utils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel


class ScanInstalledAppActivity : BaseActivity() {
    override val model: ScanInstalledAppViewModel by viewModel()
    override val binding by lazy {
        ActivityScanInstalledAppBinding.inflate(layoutInflater)
    }
    private var adapterAppInformation: CommonAdapter? = null
    lateinit var modelMalwareDetection: ModelMalwareDetection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model.codeFunction = intent.getStringExtra(Tags.CODE_FUNCTION)
        setAdapterAppInformation()
        model.percentLoading.observe(this, Observer {
            runOnUiThread {
                binding.progressBar.progress = it.toInt()
                binding.txtPercentageOfMalware.text = getString(R.string.percentage_s,it.toInt().toString())
                if (it.toInt() == 100) {
                    binding.txtPercentageOfMalware.remove()
                    binding.progressBar.remove()
                }
            }
        })
        Thread {
            handlePredictInstalledApplication()
        }.start()



    }
    private fun handlePredictInstalledApplication() {
        modelMalwareDetection = ModelMalwareDetection.newInstance(this)
        val listInstalledApplicationInfo = model.getInstalledApplication(this, modelMalwareDetection, model.codeFunction?:"")
        runOnUiThread {
            binding.rcvHomeServiceFunction.show()
            adapterAppInformation?.updateData(listInstalledApplicationInfo)
        }
        modelMalwareDetection.close()
    }


    private fun setAdapterAppInformation() {
        adapterAppInformation = CommonAdapter()
        val layoutManagerNormal = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rcvHomeServiceFunction.layoutManager = layoutManagerNormal
        binding.rcvHomeServiceFunction.adapter = adapterAppInformation
        binding.rcvHomeServiceFunction.remove()
        adapterAppInformation?.onClick = {
            if (it is InstalledApplicationInfo) {
                Log.wtf("KHANHANDEBUG", it.listPermission.toString())
            }
        }
    }

}