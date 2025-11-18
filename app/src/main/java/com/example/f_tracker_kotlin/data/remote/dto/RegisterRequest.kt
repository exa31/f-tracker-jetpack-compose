package com.example.f_tracker_kotlin.data.remote.dto

data class RegisterRequest(
    val email: String,
    val password: String,
    val name: String
)
