package com.anlmk.base.ui.activities.home
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.anlmk.base.R
import com.anlmk.base.data.`object`.CommonEntity
import com.anlmk.base.data.`object`.InstalledApplicationInfo
import com.anlmk.base.databinding.ActivityHomeBinding
import com.anlmk.base.ui.activities.login.ScanInstalledAppActivity
import com.anlmk.base.ui.adapters.CommonAdapter
import com.anlmk.base.ui.base.BaseActivity
import com.anlmk.base.utils.Tags
import org.koin.android.viewmodel.ext.android.viewModel

class HomeActivity : BaseActivity() {
    override val model: HomeViewModel by viewModel()
    override val binding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }
    private var adapterServiceHome: CommonAdapter? = null

    companion object{
        const val HOME_ALL_APP = "1"
        const val HOME_UNSAFE_APP = "2"
        const val HOME_SAFE_APP = "3"
        const val HOME_UNKNOWN_APP = "4"
    }

    override fun initView() {
        super.initView()
        setAdapterServiceHome()
    }

    override fun onListener() {
        super.onListener()
        adapterServiceHome?.onClick = {
            if (it is CommonEntity) {
                handleActionNext(it.codeFunction)
            }
        }
    }

    private fun handleActionNext(codeFunction: String?) {
        val intent = Intent(this, ScanInstalledAppActivity::class.java)
        intent.putExtra(Tags.CODE_FUNCTION, codeFunction)
        startActivity(intent)
    }


    private fun setAdapterServiceHome() {
        adapterServiceHome = CommonAdapter()
        val layoutManagerNormal = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rcvHomeServiceFunction.layoutManager = layoutManagerNormal
        binding.rcvHomeServiceFunction.adapter = adapterServiceHome
        adapterServiceHome?.updateData(model.getHomeServiceFunction())

    }


}