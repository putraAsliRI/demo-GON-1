package com.asliri.samplegon.data.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asliri.samplegon.data.Resource
import com.asliri.samplegon.data.repository.TodoRepository
import kotlinx.coroutines.launch

class TodoViewModel(private val repository: TodoRepository) : ViewModel() {

    private val _todo = MutableLiveData<TodoResponse>()
    val todo: LiveData<TodoResponse> = _todo

    private val _authUser = MutableLiveData<Resource<AuthResponse>>()
    val authUser: LiveData<Resource<AuthResponse>> = _authUser

    private val _register = MutableLiveData<Resource<AuthResponse>>()
    val register: LiveData<Resource<AuthResponse>> = _register

    fun loadTodo() {
        viewModelScope.launch {
            try {
                val data = repository.fetchTodo()
                _todo.value = data
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun authUser(authRequest: AuthRequest) {
        viewModelScope.launch {
            _authUser.value = Resource.Loading
            _authUser.value = repository.authUser(authRequest)
        }
    }

    fun register(authRequest: AuthRequest) {
        viewModelScope.launch {
            _register.value = Resource.Loading
            _register.value = repository.register(authRequest)
        }
    }
}
