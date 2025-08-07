# MasuyaShop - Aplikasi Manajemen Toko

MasuyaShop adalah aplikasi desktop berbasis Java untuk manajemen toko yang menyediakan fitur pengelolaan produk, pelanggan, dan transaksi penjualan. Aplikasi ini dibangun menggunakan Java Swing dengan database SQL Server.

## 📋 Daftar Isi

- [Fitur](#fitur)
- [Entity Relationship Diagram](#entity-relationship-diagram)
- [Schema Diagram](#schema-diagram)
- [Wireframe](#wireframe)
- [Struktur SQL](#struktur-sql)
- [Struktur File](#struktur-file)
- [Teknologi](#teknologi)
- [Instalasi](#instalasi)
- [Penggunaan](#penggunaan)

## ✨ Fitur

Aplikasi ini menyediakan fitur CRUD (Create, Read, Update, Delete) untuk:

### 🧑‍💼 Manajemen Pelanggan
- Tambah pelanggan baru
- Lihat daftar pelanggan
- Edit informasi pelanggan
- Hapus pelanggan
- Pencarian pelanggan

### 📦 Manajemen Produk
- Tambah produk baru
- Lihat daftar produk
- Edit informasi produk
- Hapus produk
- Pencarian produk

### 💰 Manajemen Transaksi
- Buat transaksi baru dengan multiple item
- Lihat daftar transaksi
- Edit transaksi
- Hapus transaksi
- Generate nomor invoice otomatis
- Perhitungan diskon bertingkat (3 level diskon)

## 🔄 Entity Relationship Diagram

<img width="6221" height="2031" alt="Blank diagram - Page 1" src="https://github.com/user-attachments/assets/d8a632c8-41ec-4ef7-93a7-7c8e83d686ef" />

## 📊 Schema Diagram

<img width="1362" height="614" alt="Untitled (8)" src="https://github.com/user-attachments/assets/4acc2472-a3a1-4c2f-8900-12155057693b" />

## 🖼️ Wireframe

Desain wireframe aplikasi dapat dilihat pada link Figma berikut:

[Wireframe MasuyaShop di Figma](https://www.figma.com/design/RCsMG5RN2UrLRMPGpv95yc/Masuya?node-id=0-1&p=f&t=02S8xMao8JkHBI9c-0)

## 📁 Struktur SQL

File SQL tersedia di link berikut:
- [Script SQL Database](https://storage.andinesia.my.id/masuya-shop/masuya-db-script.sql)
- [Backup Database (.bak)](https://storage.andinesia.my.id/masuya-shop/masuya_shop_db.bak)

Proyek ini menggunakan struktur SQL sebagai berikut:

```
masuya_shop_db/
├── 00_Database
│   ├── 01_CreateDatabase.sql
│   └── 02_DatabaseSettings.sql
├── 01_Security
│   └── 01_CreateUser_andi.sql
├── 02_Types
│   └── 01_DetailTransaksiType.sql
├── 03_Tables
│   ├── 01_Customer.sql
│   ├── 02_Produk.sql
│   ├── 03_Transaksi.sql
│   └── 04_Detail_Transaksi.sql
├── 04_Triggers
│   └── 01_trg_UpdateStokProduk.sql
├── 05_StoredProcedures
│   ├── sp_DeleteCustomer.sql
│   ├── sp_DeleteProduk.sql
│   ├── sp_DeleteTransaksi.sql
│   ├── sp_GenerateInvoiceNo.sql
│   ├── sp_InsertCustomer.sql
│   ├── sp_InsertProduk.sql
│   ├── sp_InsertTransaksi.sql
│   ├── sp_SearchCustomer.sql
│   ├── sp_SelectCustomer.sql
│   ├── sp_SelectCustomerByKode.sql
│   ├── sp_SelectFullTransaksiByNoInv.sql
│   ├── sp_SelectProduk.sql
│   ├── sp_SelectTransaksiDefault.sql
│   ├── sp_UpdateCustomer.sql
│   ├── sp_UpdateProduk.sql
│   └── sp_UpdateTransaksi.sql
```

## 📂 Struktur File

Proyek ini menggunakan arsitektur MVC (Model-View-Controller) untuk memisahkan logika bisnis, tampilan, dan kontrol:

```
MasuyaShop/
├── src/
│   ├── Controller/
│   │   ├── CustomerController.java
│   │   ├── ProductController.java
│   │   └── TransactionController.java
│   ├── Dao/
│   │   ├── CustomerDao.java
│   │   ├── ProdukDao.java
│   │   └── TransaksiDao.java
│   ├── Db/
│   │   └── DBConnection.java
│   ├── Main/
│   │   ├── MenuUtama.form
│   │   └── MenuUtama.java
│   ├── Model/
│   │   ├── Customer.java
│   │   ├── DetailTransaksi.java
│   │   ├── FullTransaksi.java
│   │   ├── Produk.java
│   │   └── Transaksi.java
│   ├── Pallete/
│   │   └── GradientPanel.java
│   ├── Util/
│   │   ├── Helper.java
│   │   └── OperationResult.java
│   └── View/
│       ├── Customer/
│       ├── Product/
│       ├── Search/
│       └── Transaksi/
└── lib/ (library eksternal)
```

## 🔧 Teknologi

- **Bahasa Pemrograman**: Java
- **UI Framework**: Java Swing dengan komponen kustom
- **Database**: Microsoft SQL Server
- **Koneksi Database**: JDBC
- **Build Tool**: Apache Ant
- **IDE**: NetBeans

## 🚀 Instalasi

1. Clone repositori ini
2. Buat database dengan menjalankan script SQL di folder `masuya_shop_db`
3. Sesuaikan konfigurasi database di `src/Db/DBConnection.java`
4. Buka proyek di NetBeans
5. Build dan jalankan aplikasi

## 💻 Penggunaan

1. Jalankan aplikasi
2. Gunakan menu navigasi di sebelah kiri untuk beralih antara manajemen Produk, Customer, dan Transaksi
3. Untuk membuat transaksi baru:
   - Pilih menu Transaksi
   - Klik tombol "Tambah"
   - Pilih customer
   - Tambahkan produk ke keranjang
   - Atur jumlah dan diskon jika diperlukan
   - Simpan transaksi

## 📝 Catatan

Aplikasi ini dikembangkan sebagai proyek pembelajaran dan dapat dikembangkan lebih lanjut dengan menambahkan fitur-fitur seperti:

- Manajemen stok otomatis
- Laporan penjualan
- Dashboard analitik
- Manajemen pengguna dengan hak akses berbeda

---

&copy; 2023 MasuyaShop. Dibuat dengan ❤️ oleh Andi.
