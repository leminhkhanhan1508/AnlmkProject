package com.anlmk.base.ui.activities.splash

import com.anlmk.base.data.impl.SplashRepo
import com.anlmk.base.di.ResourceProvider
import com.anlmk.base.ui.base.BaseViewModel

class SplashViewModel(
    private val splashRepo: SplashRepo,
    val resourcesProvider: ResourceProvider
) : BaseViewModel() {


}