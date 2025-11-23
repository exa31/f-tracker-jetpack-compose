package com.example.f_tracker_kotlin.data.repository

import com.example.f_tracker_kotlin.data.enums.TransactionType
import com.example.f_tracker_kotlin.data.enums.ViewOptions
import com.example.f_tracker_kotlin.data.remote.api.TransactionService
import com.example.f_tracker_kotlin.data.remote.dto.CreateTransactionRequest
import com.example.f_tracker_kotlin.data.remote.dto.UpdateTransactionRequest
import javax.inject.Inject

class TransactionRepository @Inject constructor(
    private val api: TransactionService
) {
    suspend fun getTransactions(view: ViewOptions) =
        api.getTransactions(view = view.label)

    suspend fun createTransaction(
        amount: Int,
        createdAt: String,
        type: TransactionType,
        description: String
    ) =
        api.createTransaction(
            CreateTransactionRequest(
                amount = amount,
                createdAt = createdAt,
                type = type.label,
                description = description
            )
        )

    suspend fun updateTransaction(
        id: String,
        amount: Int,
        createdAt: String,
        type: TransactionType,
        description: String
    ) =
        api.updateTransaction(
            transactionId = id,
            request = UpdateTransactionRequest(
                amount = amount,
                createdAt = createdAt,
                type = type.label,
                description = description
            )
        )

    suspend fun deleteTransaction(id: String) =
        api.deleteTransaction(transactionId = id)

}