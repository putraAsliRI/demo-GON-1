package com.asliri.samplegon.data.repository

import com.asliri.samplegon.data.Resource
import com.asliri.samplegon.data.model.AuthRequest
import com.asliri.samplegon.data.model.AuthResponse
import com.asliri.samplegon.data.model.TodoResponse
import com.asliri.samplegon.data.network.ApiService
import retrofit2.HttpException
import java.io.IOException

class TodoRepository(private val api: ApiService) {
    suspend fun fetchTodo(): TodoResponse = api.getTodo()

    suspend fun authUser(authRequest: AuthRequest): Resource<AuthResponse> {
        return try {
            val response = api.authUser(authRequest)
            Resource.Success(response)
        } catch (e: HttpException) {
            Resource.Error(e.code(), e.response()?.errorBody()?.string())
        } catch (e: IOException) {
            Resource.Error(null, "Network error: ${e.message}")
        } catch (e: Exception) {
            Resource.Error(null, "Unexpected error: ${e.message}")
        }
    }

    suspend fun register(authRequest: AuthRequest): Resource<AuthResponse> {
        return try {
            val response = api.register(authRequest)
            Resource.Success(response)
        } catch (e: HttpException) {
            Resource.Error(e.code(), e.response()?.errorBody()?.string())
        } catch (e: IOException) {
            Resource.Error(null, "Network error: ${e.message}")
        } catch (e: Exception) {
            Resource.Error(null, "Unexpected error: ${e.message}")
        }
    }
}