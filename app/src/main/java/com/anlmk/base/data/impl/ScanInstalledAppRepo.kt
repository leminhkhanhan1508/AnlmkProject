package com.anlmk.base.data.impl

import com.anlmk.base.data.request.LoginRequest
import com.anlmk.base.data.request.User
import com.anlmk.base.data.response.LoginResponse
import retrofit2.Response

interface ScanInstalledAppRepo{
}

class ScanInstalledAppImpl(private val service: Service): ScanInstalledAppRepo{
}