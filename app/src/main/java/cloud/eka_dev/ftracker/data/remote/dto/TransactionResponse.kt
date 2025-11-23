package cloud.eka_dev.ftracker.data.remote.dto

data class TransactionResponse(
    val current: List<Transaction>,
    val last: List<Transaction>
)