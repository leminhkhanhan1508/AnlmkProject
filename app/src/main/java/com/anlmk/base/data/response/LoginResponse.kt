package com.anlmk.base.data.response


import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("code")
    val code: String,
    @SerializedName("massage")
    val massage: String
)