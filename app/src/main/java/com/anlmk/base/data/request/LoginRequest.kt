package com.anlmk.base.data.request


import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("password")
    val password: String,
    @SerializedName("user_name")
    val userName: String
)

data class User(
    @SerializedName( "id")
    val id: Int = 0,
    @SerializedName( "name")
    val name: String = "",
    @SerializedName( "email")
    val email: String = "",
    @SerializedName( "avatar")
    val avatar: String = ""
)