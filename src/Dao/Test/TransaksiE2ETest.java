/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao.Test;

import Dao.CustomerDao;
import Dao.ProdukDao;
import Dao.TransaksiDao;
import Db.DBConnection;
import Model.Customer;
import Model.DetailTransaksi;
import Model.FullTransaksi;
import Model.Produk;
import Model.Transaksi;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author andid
 */
public class TransaksiE2ETest {

    // Utility: ambil stok produk saat ini
    private static int getStock(String kodeProduk) throws SQLException {
        String sql = "SELECT stok FROM produk WHERE kode = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, kodeProduk);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("stok");
                } else {
                    throw new IllegalArgumentException("Produk tidak ditemukan: " + kodeProduk);
                }
            }
        }
    }

    // Utility: ambil harga produk
    private static double getPrice(String kodeProduk) throws SQLException {
        String sql = "SELECT harga FROM produk WHERE kode = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, kodeProduk);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("harga").doubleValue();
                } else {
                    throw new IllegalArgumentException("Produk tidak ditemukan: " + kodeProduk);
                }
            }
        }
    }

    public static void main(String[] args) {
        // DAOs
        CustomerDao customerDao = new CustomerDao();
        ProdukDao produkDao = new ProdukDao();
        TransaksiDao trxDao = new TransaksiDao();

        // Kode unik untuk test
        String kodeCust = "CUST0005";
        String kodeProd = "PROD0005";
        String noInv = "INV0005";

        try {
            System.out.println("=== SETUP: INSERT CUSTOMER & PRODUK ===");
            // 1. INSERT CUSTOMER
            Customer cust = new Customer(
                    kodeCust, // kodeLama (tidak dipakai di insert)
                    kodeCust, // kodeBaru
                    "Test Customer",
                    "Jl. Test No.1",
                    "KelurahanT",
                    "KecamatanT",
                    "KotaT",
                    "ProvinsiT",
                    "12345"
            );
            boolean okCust = customerDao.insert(cust);
            System.out.println("Insert Customer: " + okCust);

            // 2. INSERT PRODUK
            Produk prod = new Produk(
                    kodeProd,
                    "Test Produk",
                    50000.0, // harga
                    10 // stok awal
            );
            boolean okProd = produkDao.insert(prod);
            System.out.println("Insert Produk:   " + okProd);

            // Ambil data master untuk transaksi
            int stockBefore = getStock(kodeProd);
            double price = getPrice(kodeProd);

            // Siapkan detail transaksi qty=2
            int qtyInsert = 2;
            double jumlah = price * qtyInsert;
            DetailTransaksi det = new DetailTransaksi(
                    kodeProd,
                    "Test Produk",
                    qtyInsert,
                    jumlah,
                    0.0, 0.0, 0.0,
                    jumlah
            );
            List<DetailTransaksi> details = new ArrayList<>();
            details.add(det);

            Transaksi trx = new Transaksi(
                    noInv,
                    kodeCust,
                    new Date(System.currentTimeMillis()),
                    jumlah,
                    details
            );

            System.out.println("\n=== TEST INSERT TRANSAKSI ===");
            boolean okInsert = trxDao.insertTransaksi(trx);
            int stockAfterInsert = getStock(kodeProd);
            System.out.printf("Insert returned: %b | stok: %d → %d%n",
                    okInsert, stockBefore, stockAfterInsert);
            if (!okInsert || stockAfterInsert != stockBefore - qtyInsert) {
                throw new RuntimeException("Insert transaksi / stok mismatch!");
            }

            // Verifikasi fetch
            FullTransaksi fetched1 = trxDao.getFullTransaksiByNoInv(noInv);
            if (fetched1 == null || fetched1.getDetailList().size() != 1) {
                throw new RuntimeException("Fetch after insert gagal!");
            }
            System.out.println("Fetch after insert: OK");

            System.out.println("\n=== TEST UPDATE TRANSAKSI ===");
            // Ubah qty jadi 1
            int qtyUpdate = 1;
            double jumlahUpdate = price * qtyUpdate;
            DetailTransaksi det2 = new DetailTransaksi(
                    kodeProd,
                    "Test Produk",
                    qtyUpdate,
                    jumlahUpdate,
                    0.0, 0.0, 0.0,
                    jumlahUpdate
            );
            List<DetailTransaksi> updDets = new ArrayList<>();
            updDets.add(det2);
            trx.setDetailList(updDets);
            trx.setTotal(jumlahUpdate);

            boolean okUpdate = trxDao.updateTransaksi(trx);
            int stockAfterUpdate = getStock(kodeProd);
            System.out.printf("Update returned: %b | stok: %d → %d%n",
                    okUpdate, stockBefore, stockAfterUpdate);
            if (!okUpdate || stockAfterUpdate != stockBefore - qtyUpdate) {
                throw new RuntimeException("Update transaksi / stok mismatch!");
            }

            // Verifikasi fetch update
            FullTransaksi fetched2 = trxDao.getFullTransaksiByNoInv(noInv);
            if (fetched2 == null || fetched2.getDetailList().get(0).getQty() != qtyUpdate) {
                throw new RuntimeException("Fetch after update gagal!");
            }
            System.out.println("Fetch after update: OK");

            System.out.println("\n=== TEST DELETE TRANSAKSI ===");
            boolean okDelete = trxDao.deleteTransaksi(noInv);
            int stockAfterDelete = getStock(kodeProd);
            System.out.printf("Delete returned: %b | stok: %d → %d%n",
                    okDelete, stockBefore - qtyUpdate, stockAfterDelete);
            if (!okDelete || stockAfterDelete != stockBefore) {
                throw new RuntimeException("Delete transaksi / stok mismatch!");
            }

            // Verifikasi fetch delete
            FullTransaksi fetched3 = trxDao.getFullTransaksiByNoInv(noInv);
            if (fetched3 != null) {
                throw new RuntimeException("Transaksi masih ada setelah delete!");
            }
            System.out.println("Fetch after delete: OK");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("TEST E2E FAILED: " + e.getMessage());
        } finally {
            // CLEANUP: hapus produk & customer
            System.out.println("\n=== CLEANUP: DELETE TRANSAKSI, PRODUK & CUSTOMER ===");
            // 1) Hapus transaksi dulu biar FK detail_transaksi tidak nge-blok
            if (new TransaksiDao().deleteTransaksi(noInv)) {
                System.out.println("Transaksi deleted: " + noInv);
            }
            // 2) Baru hapus produk
            if (new ProdukDao().delete(kodeProd)) {
                System.out.println("Produk deleted:    " + kodeProd);
            }
            // 3) Baru hapus customer
            if (new CustomerDao().delete(kodeCust)) {
                System.out.println("Customer deleted:  " + kodeCust);
            }
        }
    }
}
