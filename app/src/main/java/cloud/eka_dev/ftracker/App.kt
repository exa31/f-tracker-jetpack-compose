package cloud.eka_dev.ftracker

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/*
Kenapa Harus Bikin App : Application dan Tambah @HiltAndroidApp?

Ketika kamu menggunakan Hilt, dia butuh tempat pertama untuk start dan nyimpen semua dependency yang akan dipakai di seluruh app.
Tempat itu disebut Application-level dependency container.

Nah, @HiltAndroidApp itu:

Membuat root container untuk semua dependency Hilt

Ngasih tahu Hilt: "Hei, mulai inject dari sini ya!"

Mengaktifkan auto-generated kode untuk injection di Activity, Fragment, ViewModel, Worker, dsb

Itu kenapa class App harus extends Application. Karena Application adalah entry point teratas ketika app di-run.

📦 Alurnya (gambaran sederhana)

App start →
→ Android create Application instance (App) →
→ Hilt build dependency graph →
→ Baru Activity, ViewModel, dll bisa @Inject.

Kalau ini nggak ada, ViewModel-mu yang minta @Inject DI Store/DataStore/Repo bakal bilang:

Missing binding!!
Aku gak tau dapetin depedency ini dari mana!!

🧭 Kenapa Harus Tambah di AndroidManifest.xml?

Karena Android perlu tahu:

“Application class mana yang harus dipakai?”

Kalau kamu nggak daftarin:
Android tetap pakai Application default bawaan OS →
Hilt nggak akan jalan →
Injection gagal →
Errornya biasanya: Hilt... not installed, Missing binding, atau crash saat ViewModel dibuat.

Jadi kamu tambahkan:

<application
    android:name=".App"
    ... >


Artinya:

Gunakan class App ini sebagai Application utama untuk seluruh program.

📌 Kesimpulan Manis
Bagian	Kenapa Wajib	Fungsinya
class App : Application	Karena kita butuh custom Application	Tempat Hilt mulai bekerja
@HiltAndroidApp	Mengaktifkan sistem DI (Dependency Injection) Hilt	Generate root dependency graph
android:name=".App" di manifest	Biar Android tahu Application mana yang dipakai	Agar Hilt bisa aktif saat app mulai
🎨 Analogi Santai

Bayangin aplikasimu adalah kota.

Application = Pemerintahan kota

Hilt = Petugas logistik (bagi-bagi resource)

Modules = Gudang resource

@HiltAndroidApp = Ngasih SK pengangkatan ke petugas logistik

Manifest android:name=".App" = Ngasih alamat kantor pemerintah

Kalau SK-nya gak dikasih → gak ada yang ngatur resource → semua gedung (ViewModel/Repo/Fragment) bakal kebingungan.



Kapan App ini boleh / perlu ditambah?

Kalau nanti kamu butuh hal seperti:

Kebutuhan	Contoh	Tambahan di App
Inisialisasi library	Firebase, Timber, Koin (walau kamu pakai Hilt)	Tambah onCreate()
Global event tracking	Analytics, crash logger	Tambah listener di onCreate()
Global config	Dark mode dari server	Simpan state global di sini

Contoh kalau kamu mau init Timber:

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}


Contoh kalau pakai Firebase:

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}

🧼 Tapi ingat prinsip penting:

Jangan simpan UI State atau logic berat di sini.

Karena Application hidup sepanjang umur aplikasi.
Kalau kamu simpan sesuatu di sini seenaknya → rawan memory leak / data nyangkut.

💡 Ringkasnya

Boleh kosong → itu normal dan sudah benar

Nanti kamu hanya nambah kalau memang ada sesuatu yang perlu diinisialisasi global

Tidak semua apps butuh tambahan di sini



 */
@HiltAndroidApp
class App : Application()
