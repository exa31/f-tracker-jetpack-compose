package com.example.f_tracker_kotlin.data.remote.dto

data class TransactionResponse(
    val current: List<Transaction>,
    val last: List<Transaction>
)