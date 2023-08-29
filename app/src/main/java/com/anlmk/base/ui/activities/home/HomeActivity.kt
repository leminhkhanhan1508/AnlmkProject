package com.anlmk.base.ui.activities.home
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.anlmk.base.R
import com.anlmk.base.data.`object`.CommonEntity
import com.anlmk.base.databinding.ActivityHomeBinding
import com.anlmk.base.databinding.ActivityLoginBinding
import com.anlmk.base.ui.adapters.CommonAdapter
import com.anlmk.base.ui.base.BaseActivity
import org.koin.android.viewmodel.ext.android.viewModel

class HomeActivity : BaseActivity() {
    override val model: HomeViewModel by viewModel()
    override val binding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }
    private var adapterServiceHome: CommonAdapter? = null
    private var adapterBottomBarHome: CommonAdapter? = null
    private val spanNormalFunctionView= 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initListener()
    }

    private fun initListener() {
        adapterServiceHome?.onClick = {
            if (it is CommonEntity) {
                Log.wtf("TEST ACTION HOME", it.getTitle())
            }
        }
        adapterBottomBarHome?.onClick = {
            if (it is CommonEntity) {
                Log.wtf("TEST ACTION HOME", it.getTitle())
            }
        }
    }

    private fun initView() {
        setAdapterServiceHome()
        setAdapterBottomBarHome()
    }
    private fun setAdapterServiceHome() {
        adapterServiceHome = CommonAdapter()
        val layoutManagerNormal = GridLayoutManager(this, spanNormalFunctionView)
        layoutManagerNormal.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (adapterServiceHome?.getItemViewType(position)) {
                    CommonAdapter.HEADER -> spanNormalFunctionView
                    else -> 1
                }
            }
        }
        binding.rcvHomeServiceFunction.layoutManager = layoutManagerNormal
        binding.rcvHomeServiceFunction.adapter = adapterServiceHome
        adapterServiceHome?.updateData(model.getHomeServiceFunction())
    }

    private fun setAdapterBottomBarHome() {
        adapterBottomBarHome = CommonAdapter()
        val layoutManagerNormal =  GridLayoutManager(this, model.getHomeBottomBarFunction().size)
        binding.rcvHomeBottomBar.layoutManager = layoutManagerNormal
        binding.rcvHomeBottomBar.adapter = adapterBottomBarHome
        adapterBottomBarHome?.updateData(model.getHomeBottomBarFunction())
    }


}