package com.asliri.samplegon.data.model

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    val code: Int,
    val message: String,
    val result: ResultData
)

data class ResultData(
    @SerializedName("session_code")
    val sessionCode: String
)