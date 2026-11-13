# Campus Catalog — Tugas Pertemuan 7 (Room + DataStore)

Kelanjutan project P6. Aplikasi katalog course kampus dengan data utama di **Room** dan preference pengguna di **DataStore**.

- Nama: `M IKMAL`
- NIM: `0102523033`
- Mata kuliah: Pemrograman Bergerak, Informatika UAI

## Fitur
- Daftar course (list), tambah/edit course (form), pengaturan (settings): 3 destination.
- Tambah, ubah, dan hapus data tersimpan di database Room.
- Pengaturan **Dark mode** dan **Urutan data** (nama / terbaru / SKS) tersimpan di DataStore.
- Data dan pengaturan tetap ada setelah aplikasi ditutup dan dibuka kembali.

## Arsitektur

```
UI (Compose)  ->  ViewModel (UiState/StateFlow)  ->  Repository  ->  Room (data utama)
                                                                  ->  DataStore (preference)
```

| Lapisan | File |
|---|---|
| Entity | `data/local/CatalogItemEntity.kt` (6 field, primary key auto-generate) |
| DAO | `data/local/CatalogItemDao.kt` (Flow read, insert, update, delete, getById) |
| Database | `data/local/CatalogDatabase.kt` (singleton, bukan di Composable) |
| DataStore | `data/prefs/UserPreferencesDataStore.kt` (dark_mode, sort_order) |
| Repository | `data/CatalogRepository.kt` (satu-satunya entry point data layer) |
| ViewModel | `ui/CatalogViewModel.kt` (menggabungkan Flow Room + DataStore menjadi `CatalogUiState`) |
| UI | `ui/screens/*`, `MainActivity.kt` |

Aturan yang dipatuhi: tidak ada list hard-coded, tidak ada DAO dipanggil dari Composable, DataStore tidak menggantikan tabel utama.

## Cara menjalankan
1. Buka folder project di Android Studio (Ladybug/Koala atau lebih baru), tunggu Gradle sync.
2. Jalankan di emulator/perangkat (minSdk 24).
3. Test persistence otomatis: `./gradlew connectedAndroidTest` (butuh emulator aktif).

## Cara uji persistence manual
1. Tambah beberapa course, ubah dark mode dan urutan di Settings.
2. Tutup aplikasi dari recent apps (swipe), lalu buka lagi.
3. Data dan pengaturan masih sama.

## Deklarasi penggunaan AI
`- Saya menggunakan AI generatif sebagai alat bantu.
  Tools yang digunakan: Claude AI`
