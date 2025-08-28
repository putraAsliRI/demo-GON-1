package com.asliri.samplegon.data.model

data class AuthRequest(
    val phone: String,
    val email: String,
    val app_id: String,
    val transaction_ext_id: String? = null,
    val transaction_type: String? = null
)