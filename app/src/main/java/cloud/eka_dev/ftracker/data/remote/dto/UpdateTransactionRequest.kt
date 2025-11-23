package cloud.eka_dev.ftracker.data.remote.dto

data class UpdateTransactionRequest(
    val amount: Int,
    val type: String,
    val description: String,
    val createdAt: String
)