package com.anlmk.base.ui.activities.home
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.anlmk.base.data.`object`.CommonEntity
import com.anlmk.base.databinding.ActivityHomeBinding
import com.anlmk.base.extensions.setSafeOnClickListener
import com.anlmk.base.ui.activities.addMealTime.AddMealTimeActivity
import com.anlmk.base.ui.adapters.CommonAdapter
import com.anlmk.base.ui.base.BaseActivity
import com.anlmk.base.utils.RequestCode
import okhttp3.internal.EMPTY_REQUEST
import org.koin.android.viewmodel.ext.android.viewModel

class HomeActivity : BaseActivity() {
    override val model: HomeViewModel by viewModel()
    override val binding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }
    private var adapterMealOfDateHome: CommonAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initListener()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            RequestCode.REQUEST_ADD_MEAL->{
                if (resultCode == RESULT_OK){
                    model.getMealTimeList()
                }
            }
        }
    }

    private fun initListener() {
        adapterMealOfDateHome?.onClick = {
            if (it is CommonEntity) {
                Log.wtf("TEST ACTION HOME", it.getTitle())
            }
        }
        binding.btnAddInformation.setSafeOnClickListener {
            startActivityForResult(Intent(this,AddMealTimeActivity::class.java), RequestCode.REQUEST_ADD_MEAL)
        }
    }

   override fun initView() {
       model.getMealTimeList()
        setAdapterServiceHome()
    }

    override fun onObserveData() {
        super.onObserveData()
        model.listMealTimeLive.observe(this, Observer {
            adapterMealOfDateHome?.updateData(it)
        })
    }
    private fun setAdapterServiceHome() {
        adapterMealOfDateHome = CommonAdapter()
        binding.rcvMealOfDateHome.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.rcvMealOfDateHome.adapter = adapterMealOfDateHome
    }
}