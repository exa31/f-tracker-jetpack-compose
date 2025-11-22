package com.example.f_tracker_kotlin.data.remote.dto

data class CreateTransactionRequest(
    val amount: Int,
    val type: String,
    val description: String,
    val createdAt: String
)