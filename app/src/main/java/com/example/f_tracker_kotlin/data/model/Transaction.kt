package com.example.f_tracker_kotlin.data.model

data class Transaction(
    val id: Int,
    val title: String,
    val amount: Long,
    val date: String
)

