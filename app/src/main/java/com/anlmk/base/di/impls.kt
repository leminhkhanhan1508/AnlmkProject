package com.anlmk.base.di

import com.anlmk.base.data.impl.*
import org.koin.dsl.module

val impls = module {
    single<LoginRepo> {
        LoginRepoImpl(
            get()
        )
    }

    single<SplashRepo> {
        SplashRepoImpl(
            get()
        )
    }

    single<HomeRepo> {
        HomeRepoImpl(
            get()
        )
    }
}