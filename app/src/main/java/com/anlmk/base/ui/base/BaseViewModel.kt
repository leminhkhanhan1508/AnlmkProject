package com.anlmk.base.ui.base

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anlmk.base.extensions.safeLog
import com.anlmk.base.utils.SingleLiveEvent
import kotlinx.coroutines.*
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLPeerUnverifiedException

abstract class BaseViewModel : ViewModel() {
    val isLoading = MutableLiveData<Boolean>().apply { value = false }
    val noInternetConnectionEvent = SingleLiveEvent<Unit>()
    val connectTimeoutEvent = SingleLiveEvent<Unit>()
    val invalidCertificateEvent = SingleLiveEvent<Unit>()
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        viewModelScope.launch {
            throwable.safeLog()
            withContext(Dispatchers.Main) {
                hideLoading()
                when (throwable) {
                    is UnknownHostException -> {
                        noInternetConnectionEvent.call()
                    }
                    is SocketTimeoutException -> {
                        connectTimeoutEvent.call()
                    }
                    is SSLPeerUnverifiedException -> {
                        invalidCertificateEvent.call()
                    }
                    else -> {
                        onError(throwable)
                    }
                }
            }
        }
    }
    private val network = viewModelScope + exceptionHandler
    fun showLoading() {
        isLoading.value = true
    }

    fun hideLoading() {
        isLoading.value = false
    }

    fun launch(block: suspend CoroutineScope.() -> Unit): Job {
        showLoading()
        return network.launch {
            block.invoke(network)
            withContext(Dispatchers.Main) {
                hideLoading()
            }
            return@launch
        }
    }

    open fun onError(cause: Throwable) {}

}