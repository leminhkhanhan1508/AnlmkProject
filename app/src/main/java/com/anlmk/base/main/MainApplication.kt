package com.anlmk.base.main
import android.app.Application
import android.util.Log
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.logger.AndroidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MainApplication : MultiDexApplication() {
    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        try {
            startKoin {
                logger(AndroidLogger(Level.NONE))
                androidContext(this@MainApplication)
                modules(com.anlmk.base.di.modules)
            }
        } catch (e: Exception) {
            Log.wtf("startKoin-Error", e.message)
        }
    }

    companion object {
        private var instance: MainApplication? = null

        fun getInstanceApp(): MainApplication {
            return instance as MainApplication
        }
    }
}
