package com.anlmk.base.data.impl

import com.anlmk.base.data.request.LoginRequest
import com.anlmk.base.data.request.User
import com.anlmk.base.data.response.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface Service {
    @POST("login/")
    suspend fun login(@Body data: LoginRequest): LoginResponse

    @GET("users")
    suspend fun getUsers(): Response<List<User>>
}