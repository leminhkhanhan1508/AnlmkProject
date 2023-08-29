package com.anlmk.base.ui.activities.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.anlmk.base.R
import com.anlmk.base.data.impl.LoginRepo
import com.anlmk.base.data.`object`.CommonEntity
import com.anlmk.base.data.response.LoginResponse
import com.anlmk.base.di.ResourceProvider
import com.anlmk.base.ui.adapters.CommonAdapter
import com.anlmk.base.ui.base.BaseViewModel

class HomeViewModel(
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

    fun getHomeServiceFunction(): MutableList<CommonEntity> {
        return  mutableListOf(
            CommonEntity().apply {
                this.setTitle("Tiêu đề của chức năng")
                this.setTypeLayout(CommonAdapter.HEADER)
            },
            CommonEntity().apply {
                this.setTitle(resourcesProvider.getString(R.string.splash_hello))
                this.setDescript(resourcesProvider.getString(R.string.splash_hello))
                this.setIcon(R.drawable.baseline_notifications_24)
            },
            CommonEntity().apply {
                this.setTitle(resourcesProvider.getString(R.string.splash_hello))
                this.setDescript(resourcesProvider.getString(R.string.splash_hello))
                this.setIcon(R.drawable.baseline_notifications_24)
            },
            CommonEntity().apply {
                this.setTitle(resourcesProvider.getString(R.string.splash_hello))
                this.setDescript(resourcesProvider.getString(R.string.splash_hello))
                this.setIcon(R.drawable.baseline_notifications_24)
            },
            CommonEntity().apply {
                this.setTitle(resourcesProvider.getString(R.string.splash_hello))
                this.setDescript(resourcesProvider.getString(R.string.splash_hello))
                this.setIcon(R.drawable.baseline_notifications_24)
            },
            CommonEntity().apply {
                this.setTitle(resourcesProvider.getString(R.string.splash_hello))
                this.setDescript(resourcesProvider.getString(R.string.splash_hello))
                this.setIcon(R.drawable.baseline_notifications_24)
            },
            CommonEntity().apply {
                this.setTitle(resourcesProvider.getString(R.string.app_name))
                this.setDescript(resourcesProvider.getString(R.string.splash_hello))
                this.setIcon(R.drawable.baseline_notifications_24)
            }
        )
    }

    fun getHomeBottomBarFunction(): MutableList<CommonEntity> {
        return  mutableListOf(
            CommonEntity().apply {
                this.setTitle(resourcesProvider.getString(R.string.home_page))
                this.setDescript(resourcesProvider.getString(R.string.splash_hello))
                this.setIcon(R.drawable.baseline_home_24)
            },
            CommonEntity().apply {
                this.setTitle(resourcesProvider.getString(R.string.setting_page))
                this.setDescript(resourcesProvider.getString(R.string.splash_hello))
                this.setIcon(R.drawable.baseline_settings_suggest_24)
            },
            CommonEntity().apply {
                this.setTitle(resourcesProvider.getString(R.string.other_page))
                this.setDescript(resourcesProvider.getString(R.string.splash_hello))
                this.setIcon(R.drawable.baseline_scatter_plot_24)
            }
        )
    }
}