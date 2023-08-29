package com.anlmk.base.ui.activities.splash

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.anlmk.base.data.impl.LoginRepo
import com.anlmk.base.data.impl.SplashRepo
import com.anlmk.base.data.response.LoginResponse
import com.anlmk.base.di.ResourceProvider
import com.anlmk.base.ui.base.BaseViewModel

class SplashViewModel(
    private val splashRepo: SplashRepo,
    val resourcesProvider: ResourceProvider
) : BaseViewModel() {


}