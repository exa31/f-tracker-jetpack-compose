package cloud.eka_dev.ftracker.di

import android.content.Context
import cloud.eka_dev.ftracker.data.local.DataStoreManager
import cloud.eka_dev.ftracker.data.remote.api.AuthService
import cloud.eka_dev.ftracker.data.remote.api.TransactionService
import cloud.eka_dev.ftracker.data.repository.AuthRepository
import cloud.eka_dev.ftracker.data.repository.TransactionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/*
AppModule ini tempat untuk memberi tahu Hilt bagaimana cara membuat (provide) objek yang akan di-inject ke ViewModel / Activity / Composables.

Bayangin ini seperti pabrik resmi dari dependency yang dibutuhkan aplikasi.

🧱 Penjelasan Baris per Baris
@Module

Menandakan file ini adalah kumpulan provider dependency.

@InstallIn(SingletonComponent::class)

Artinya semua dependency yang dibuat di module ini akan hidup selama aplikasi berjalan (global, 1 instance saja).

@Provides

Menandakan fungsi ini bertanggung jawab membuat objek.

@Singleton

Objek yang dihasilkan hanya 1 selama aplikasi hidup (bukan dibuat ulang berkali-kali).

🔌 Detail Fungsinya
1) DataStoreManager
fun provideDataStoreManager(@ApplicationContext context: Context) = DataStoreManager(context)


@ApplicationContext → minta context global, bukan context Activity

Dipake buat DataStore, dan ini wajib pakai context aplikasi

Karena DataStore harus 1 instance → dikasih @Singleton

2) AuthRepository
fun provideAuthRepository() = AuthRepository()


Kalau AuthRepository butuh API client/retrofit/dll nanti bisa juga di-inject di sini

Untuk sekarang masih sederhana → cukup return instance-nya

💫 Cara Pakainya di ViewModel

ViewModel kamu:

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repo: AuthRepository,
    private val store: DataStoreManager
) : ViewModel()


Tinggal kasih @Inject constructor, Hilt langsung tau cara membangun ViewModel ini karena provider-nya sudah ada di AppModule.

📌 Kenapa Harus Bikin Module?

Karena:

Hilt nggak bisa nebak sendiri bagaimana cara membuat DataStoreManager (perlu context).

Kita ingin instance AuthRepository & DataStoreManager konsisten dan tidak duplikat.

Jadi module = peta pabrik dependency.

💡 Singkatnya
File	Peran
App.kt	Aktifkan Hilt & jadi root container
AppModule.kt	Tempat bikin & daftarin dependency global
LoginViewModel	Menerima dependency lewat @Inject constructor



Tidak selalu cuma AppModule. Itu tergantung kebutuhan aplikasi kamu berkembang sampai mana.
Tapi AppModule biasanya memang jadi module dasar yang selalu ada. 🌱

📦 Kenapa bisa ada module lain?

Karena setiap module biasanya memetakan kelompok dependency tertentu.

Bayangkan kaya lemari-lemari:

Module	Isi / Tugas	Kapan dipakai
AppModule	Object global (DataStore, Repo, API Client)	Hampir pasti selalu ada
NetworkModule	Retrofit, OkHttp, Gson, Interceptor	Kalau kamu connect ke backend
DatabaseModule	Room Database, DAO, migrations	Kalau kamu pakai Room
RepositoryModule	Bind interface → repository implementation	Kalau kamu pakai pattern Clean Architecture
UseCaseModule	Provide UseCases	Kalau struktur makin besar (MVVM + Clean)
🎒 Contoh Saat Aplikasi Bertambah Kompleks

Misal kamu pakai Retrofit, nanti muncul module baru:

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttp(): OkHttpClient = OkHttpClient.Builder().build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://example.com")
            .client(client)
            .build()
}


Kalau kamu pakai Room:

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) =
        AppDatabase.getInstance(context)

    @Provides
    fun provideUserDao(db: AppDatabase) = db.userDao()
}

🌾 Jadi Jawabannya:
Tidak wajib hanya satu module

Tapi:

Untuk app sederhana → satu AppModule saja sudah cukup.

Kalau aplikasi makin besar → kita pisah-pisah biar lebih rapi dan maintainable.

✨ Intuisi Ringkas:

Awalnya:

AppModule   ✅


Ketika aplikasi tumbuh:

AppModule
NetworkModule
DatabaseModule
RepositoryModule
UseCaseModule


Semakin jelas struktur → semakin nyaman ngoding dan debugging.

 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDataStoreManager(@ApplicationContext context: Context) = DataStoreManager(context)

    @Provides
    @Singleton
    fun provideAuthRepository(api: AuthService) = AuthRepository(api = api)

    @Provides
    @Singleton
    fun provideTransactionRepository(api: TransactionService) = TransactionRepository(api = api)
}
