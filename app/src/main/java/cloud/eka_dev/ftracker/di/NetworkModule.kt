package cloud.eka_dev.ftracker.di

import cloud.eka_dev.ftracker.BuildConfig
import cloud.eka_dev.ftracker.data.local.DataStoreManager
import cloud.eka_dev.ftracker.data.remote.api.AuthService
import cloud.eka_dev.ftracker.data.remote.api.TransactionService
import cloud.eka_dev.ftracker.data.remote.interceptor.AuthInterceptor
import cloud.eka_dev.ftracker.data.remote.interceptor.TokenAuthenticator
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
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit): AuthService =
        retrofit.create(AuthService::class.java)

    @Provides
    @Singleton
    fun provideTransactionService(retrofit: Retrofit): TransactionService =
        retrofit.create(TransactionService::class.java)

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
