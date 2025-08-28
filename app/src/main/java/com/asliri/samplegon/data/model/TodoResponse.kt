package com.asliri.samplegon.data.model

data class TodoResponse(
    val userId: Int,
    val id: Int,
    val title: String,
    val completed: Boolean
)