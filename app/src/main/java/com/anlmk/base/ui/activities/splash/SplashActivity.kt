package com.anlmk.base.ui.activities.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.anlmk.base.databinding.ActivitySplashBinding
import com.anlmk.base.ui.activities.home.HomeActivity
import com.anlmk.base.ui.activities.login.ScanInstalledAppActivity
import com.anlmk.base.ui.base.BaseActivity
import org.koin.android.viewmodel.ext.android.viewModel

@SuppressLint("CustomSplashScreen")
class SplashActivity:BaseActivity() {
    override val model: SplashViewModel by viewModel()
    override val binding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Handler().postDelayed(Runnable {
            startActivity(Intent(this,
                HomeActivity::class.java))
            finish()
        }, 400)

    }
}