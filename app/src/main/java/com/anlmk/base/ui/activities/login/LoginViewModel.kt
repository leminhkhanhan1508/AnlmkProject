package com.anlmk.base.ui.activities.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.anlmk.base.data.impl.LoginRepo
import com.anlmk.base.data.response.LoginResponse
import com.anlmk.base.di.ResourceProvider
import com.anlmk.base.ui.base.BaseViewModel

class LoginViewModel(
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

}