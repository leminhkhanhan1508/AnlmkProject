package com.anlmk.base.di

import com.anlmk.base.ui.activities.home.HomeViewModel
import com.anlmk.base.ui.activities.login.ScanInstalledAppViewModel
import com.anlmk.base.ui.activities.splash.SplashViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val models = module {
    viewModel {
        ScanInstalledAppViewModel(get(), get())
    }
    viewModel {
        SplashViewModel(get(), get())
    }
    viewModel {
        HomeViewModel(get(), get())
    }
}


