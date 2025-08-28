package com.asliri.samplegon.data.network

import com.asliri.samplegon.data.model.AuthRequest
import com.asliri.samplegon.data.model.AuthResponse
import com.asliri.samplegon.data.model.TodoResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("todos/1")
    suspend fun getTodo(): TodoResponse

    @POST("api/v1/authorize")
    suspend fun authUser(@Body auth: AuthRequest): AuthResponse

    @POST("api/v1/register")
    suspend fun register(@Body auth: AuthRequest): AuthResponse

}