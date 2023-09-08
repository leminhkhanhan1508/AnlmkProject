package com.anlmk.base.ui.activities.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.anlmk.base.R
import com.anlmk.base.data.impl.HomeRepo
import com.anlmk.base.data.`object`.CommonEntity
import com.anlmk.base.data.response.LoginResponse
import com.anlmk.base.di.ResourceProvider
import com.anlmk.base.ui.adapters.CommonAdapter
import com.anlmk.base.ui.base.BaseViewModel

class HomeViewModel(
    private val homeRepo: HomeRepo,
    val resourcesProvider: ResourceProvider
) : BaseViewModel() {
    val loginResponse = MutableLiveData<LoginResponse>()
    fun login(userName: String, password: String) =
        launch {
            val login = homeRepo.login(userName, password)
            loginResponse.postValue(login)
        }


    fun getuser() =
        launch {
            val response = homeRepo.getUsers()
            Log.d("KHANHAN", response.message())
        }

    fun getHomeServiceFunction(): MutableList<CommonEntity> {
        return mutableListOf(
            CommonEntity().apply {
                this.setTitle(resourcesProvider.getString(R.string.all_application))
                this.setDescript(resourcesProvider.getString(R.string.description_all_application))
                this.codeFunction = HomeActivity.HOME_ALL_APP
                this.setIcon(R.drawable.baseline_grid_view_24)
                this.setTypeLayout(CommonAdapter.MENU_SERVICE)
            },
            CommonEntity().apply {
                this.setTitle(resourcesProvider.getString(R.string.unsafe_application))
                this.setDescript(resourcesProvider.getString(R.string.description_unsafe_application))
                this.codeFunction = HomeActivity.HOME_UNSAFE_APP
                this.setIcon(R.drawable.baseline_gpp_maybe_24)
                this.setTypeLayout(CommonAdapter.MENU_SERVICE)
            },
            CommonEntity().apply {
                this.setTitle(resourcesProvider.getString(R.string.safe_application))
                this.codeFunction = HomeActivity.HOME_SAFE_APP
                this.setDescript(resourcesProvider.getString(R.string.description_safe_application))
                this.setIcon(R.drawable.baseline_gpp_good_24)
                this.setTypeLayout(CommonAdapter.MENU_SERVICE)
            },
            CommonEntity().apply {
                this.setTitle(resourcesProvider.getString(R.string.unknown_application))
                this.codeFunction = HomeActivity.HOME_UNKNOWN_APP
                this.setDescript(resourcesProvider.getString(R.string.description_unknown_application))
                this.setIcon(R.drawable.baseline_dangerous_24)
                this.setTypeLayout(CommonAdapter.MENU_SERVICE)
            }
        )
    }

    fun getHomeBottomBarFunction(): MutableList<CommonEntity> {
        return mutableListOf(
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