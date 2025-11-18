package com.example.f_tracker_kotlin.di

import com.example.f_tracker_kotlin.data.local.DataStoreManager
import com.example.f_tracker_kotlin.data.remote.api.AuthService
import com.example.f_tracker_kotlin.data.remote.interceptor.AuthInterceptor
import com.example.f_tracker_kotlin.data.remote.interceptor.TokenAuthenticator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideHttpClient(
        dataStore: DataStoreManager,
        @Named("refreshApi") refreshAuthService: AuthService
    ): OkHttpClient =
        OkHttpClient.Builder()
            .authenticator(TokenAuthenticator(dataStore, refreshAuthService))
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor(AuthInterceptor(dataStore))
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://be-ftracker.eka-dev.cloud/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit): AuthService =
        retrofit.create(AuthService::class.java)

    // Retrofit khusus refresh token (tanpa authenticator & interceptor)
    @Provides
    @Singleton
    @Named("refreshRetrofit")
    fun provideRefreshRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://be-ftracker.eka-dev.cloud/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    @Named("refreshApi")
    fun provideRefreshAuthService(
        @Named("refreshRetrofit") refreshRetrofit: Retrofit
    ): AuthService =
        refreshRetrofit.create(AuthService::class.java)
}
