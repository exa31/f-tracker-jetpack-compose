package cloud.eka_dev.ftracker.data.repository

import cloud.eka_dev.ftracker.data.enums.TransactionType
import cloud.eka_dev.ftracker.data.enums.ViewOptions
import cloud.eka_dev.ftracker.data.remote.api.TransactionService
import cloud.eka_dev.ftracker.data.remote.dto.CreateTransactionRequest
import cloud.eka_dev.ftracker.data.remote.dto.UpdateTransactionRequest
import javax.inject.Inject

class TransactionRepository @Inject constructor(
    private val api: TransactionService
) {
    fun getTransactionsCall(view: ViewOptions) =
        api.getTransactionsCall(view = view.label)

    suspend fun getTransactionDetail(id: String) =
        api.getTransactionDetail(transactionId = id)

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