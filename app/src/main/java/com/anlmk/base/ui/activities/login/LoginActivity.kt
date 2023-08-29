package com.anlmk.base.ui.activities.login
import android.os.Bundle
import androidx.lifecycle.Observer
import com.anlmk.base.databinding.ActivityLoginBinding
import com.anlmk.base.ui.base.BaseActivity
import org.koin.android.viewmodel.ext.android.viewModel

class LoginActivity : BaseActivity() {
    override val model: LoginViewModel by viewModel()
    override val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        demoCallApiLogin("","")
        demoReceiveResponse()
    }

    private fun demoShowDialog() {
        confirm.newBuild().setNotice("nội dung thông báo ghi vào đây")
    }

    private fun demoCallApiLogin(userName: String, password: String) {
        model.login(userName, password)
    }

    private fun demoReceiveResponse() {
        model.apply {
            loginResponse.observe(this@LoginActivity, Observer {
                //handle login in here
            })
        }
    }
}