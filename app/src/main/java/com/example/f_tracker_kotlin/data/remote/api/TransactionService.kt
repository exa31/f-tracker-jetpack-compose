package com.example.f_tracker_kotlin.data.remote.api

import com.example.f_tracker_kotlin.data.enums.ViewOptions
import com.example.f_tracker_kotlin.data.remote.dto.BaseResponse
import com.example.f_tracker_kotlin.data.remote.dto.CreateTransactionRequest
import com.example.f_tracker_kotlin.data.remote.dto.TransactionResponse
import com.example.f_tracker_kotlin.data.remote.dto.UpdateTransactionRequest
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
    suspend fun getTransactions(@Query("view") view: String = ViewOptions.MONTH.label): BaseResponse<TransactionResponse>

    @POST("api/v1/transactions")
    suspend fun createTransaction(@Body request: CreateTransactionRequest): BaseResponse<Objects>

    @PUT("/api/v1/transactions/{transactionId}")
    suspend fun updateTransaction(
        @Path("transactionId") transactionId: String,
        @Body request: UpdateTransactionRequest
    ): BaseResponse<String>

    @DELETE("/api/v1/transactions/{transactionId}")
    suspend fun deleteTransaction(
        @Path("transactionId") transactionId: String,
    ): BaseResponse<String>
}