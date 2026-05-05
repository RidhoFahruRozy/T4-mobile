# StudentDirectoryApp

Aplikasi Android untuk mengelola data direktori mahasiswa, dibuat sebagai Tugas Akhir Praktikum Penyimpanan Data.

## Nama & NIM
> Isi nama dan NIM kamu di sini

## Deskripsi Aplikasi
StudentDirectoryApp adalah aplikasi Android yang mengimplementasikan **tiga metode penyimpanan data** sekaligus dalam satu project:

- **SharedPreferences** — untuk autentikasi (login, remember me, settings)
- **Room Database** — untuk CRUD data mahasiswa (nama, NIM, prodi, email, semester)
- **Internal Storage** — untuk catatan per mahasiswa (file .txt private)

## Fitur
- Login dengan Remember Me (username: `admin`, password: `123456`)
- Daftar mahasiswa dengan RecyclerView
- Tambah, edit, hapus mahasiswa (CRUD lengkap)
- Pencarian berdasarkan nama atau NIM
- Catatan per mahasiswa (disimpan ke file)
- Pengaturan Dark Mode & Notifikasi
- Navigasi dengan Bottom Navigation + Navigation Component

## Screenshot
> Tambahkan screenshot di folder `screenshots/` dan referensikan di sini

## Metode Penyimpanan yang Digunakan

| Fitur | Metode | Alasan |
|---|---|---|
| Login & Settings | SharedPreferences | Data key-value sederhana (String, Boolean) — tidak perlu tabel |
| Data Mahasiswa | Room Database | Data terstruktur (banyak kolom), butuh CRUD dan search |
| Catatan per Mahasiswa | Internal Storage | Data berupa teks panjang per entity — lebih cocok file daripada kolom |

## Kendala & Solusi
> Isi kendala yang kamu hadapi saat mengerjakan

## Cara Build
1. Clone repository ini
2. Buka dengan Android Studio (versi Hedgehog ke atas)
3. Sync Gradle
4. Jalankan di emulator/device minimum API 24
