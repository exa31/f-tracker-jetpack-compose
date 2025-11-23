package cloud.eka_dev.ftracker.data.model

import cloud.eka_dev.ftracker.data.remote.dto.TransactionResponse

data class Transaction(
    val _id: String,
    val user: String,
    val amount: Int,
    val type: String,
    val description: String,
    val updatedAt: String,
    val createdAt: String
) {
    companion object {
        fun fromDtoCurrent(data: TransactionResponse): List<Transaction> {
            return data.current.map {
                Transaction(
                    _id = it._id,
                    user = it.user,
                    amount = it.amount,
                    type = it.type,
                    description = it.description,
                    updatedAt = it.updatedAt,
                    createdAt = it.createdAt
                )
            }
        }


    }
}

