package cloud.eka_dev.ftracker.data.remote.api

import cloud.eka_dev.ftracker.data.enums.ViewOptions
import cloud.eka_dev.ftracker.data.remote.dto.BaseResponse
import cloud.eka_dev.ftracker.data.remote.dto.CreateTransactionRequest
import cloud.eka_dev.ftracker.data.remote.dto.Transaction
import cloud.eka_dev.ftracker.data.remote.dto.TransactionResponse
import cloud.eka_dev.ftracker.data.remote.dto.UpdateTransactionRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.Objects

interface TransactionService {
    @GET("api/v1/transactions")
    fun getTransactionsCall(@Query("view") view: String = ViewOptions.MONTH.label): Call<BaseResponse<TransactionResponse>>

    @GET("api/v1/transactions/{transactionId}")
    suspend fun getTransactionDetail(
        @Path("transactionId") transactionId: String,
    ): BaseResponse<Transaction>

    @POST("api/v1/transactions")
    suspend fun createTransaction(@Body request: CreateTransactionRequest): BaseResponse<Objects>

    @PUT("/api/v1/transactions/{transactionId}")
    suspend fun updateTransaction(
        @Path("transactionId") transactionId: String,
        @Body request: UpdateTransactionRequest
    ): BaseResponse<Objects>

    @DELETE("/api/v1/transactions/{transactionId}")
    suspend fun deleteTransaction(
        @Path("transactionId") transactionId: String,
    ): BaseResponse<String>
}