package com.example.f_tracker_kotlin.data.remote.dto

data class Transaction(
    val _id: String,
    val user: String,
    val amount: Int,
    val type: String,
    val description: String,
    val updatedAt: String,
    val createdAt: String
)
