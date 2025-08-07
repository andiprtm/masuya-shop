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
public class TransaksiMultiProductTest {
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
        CustomerDao customerDao = new CustomerDao();
        ProdukDao produkDao     = new ProdukDao();
        TransaksiDao trxDao     = new TransaksiDao();

        // Kode unik untuk test
        String kodeCust     = "CUSTMP";
        String kodeProd1    = "PRODMP1";
        String kodeProd2    = "PRODMP2";
        String noInvMulti   = "INVMULTI1";

        try {
            System.out.println("=== SETUP: INSERT CUSTOMER & 2 PRODUK ===");
            // 1. INSERT CUSTOMER
            Customer cust = new Customer(
                kodeCust,    // kodeLama (ignored pada insert)
                kodeCust,    // kodeBaru
                "Customer Multi",
                "Jl. Multi No.1",
                "KelMp",
                "KecMp",
                "KotaMp",
                "ProvMp",
                "00001"
            );
            System.out.println("Insert Customer: " + customerDao.insert(cust));

            // 2. INSERT PRODUK 1
            Produk prod1 = new Produk(kodeProd1, "Produk Multi-1", 10000.0, 5);
            System.out.println("Insert Produk1:   " + produkDao.insert(prod1));

            //    INSERT PRODUK 2
            Produk prod2 = new Produk(kodeProd2, "Produk Multi-2", 20000.0, 3);
            System.out.println("Insert Produk2:   " + produkDao.insert(prod2));

            // Ambil data master
            int stock1Before = getStock(kodeProd1);
            int stock2Before = getStock(kodeProd2);
            double price1    = getPrice(kodeProd1);
            double price2    = getPrice(kodeProd2);

            // Siapkan dua detail: qty1=2, qty2=1
            int qty1 = 2, qty2 = 1;
            double jumlah1 = price1 * qty1;
            double jumlah2 = price2 * qty2;

            DetailTransaksi d1 = new DetailTransaksi(
                kodeProd1, "Produk Multi-1", qty1, jumlah1, 0.0, 0.0, 0.0, jumlah1
            );
            DetailTransaksi d2 = new DetailTransaksi(
                kodeProd2, "Produk Multi-2", qty2, jumlah2, 0.0, 0.0, 0.0, jumlah2
            );
            List<DetailTransaksi> details = new ArrayList<>();
            details.add(d1);
            details.add(d2);

            double total = jumlah1 + jumlah2;
            Transaksi trx = new Transaksi(
                noInvMulti, kodeCust, new Date(System.currentTimeMillis()), total, details
            );

            System.out.println("\n=== TEST INSERT MULTI-PRODUCT TRANSAKSI ===");
            boolean okInsert = trxDao.insertTransaksi(trx);
            int stock1After = getStock(kodeProd1);
            int stock2After = getStock(kodeProd2);
            System.out.printf("Insert returned: %b | stok1: %d→%d, stok2: %d→%d%n",
                okInsert, stock1Before, stock1After, stock2Before, stock2After);

            if (!okInsert
             || stock1After != stock1Before - qty1
             || stock2After != stock2Before - qty2) {
                throw new RuntimeException("Insert multi-product / stok mismatch!");
            }

            // Verifikasi fetch
            FullTransaksi fetched = trxDao.getFullTransaksiByNoInv(noInvMulti);
            if (fetched == null || fetched.getDetailList().size() != 2) {
                throw new RuntimeException("Fetch after insert multi-product gagal!");
            }
            System.out.println("Fetch after insert multi-product: OK");

            // --- TEST UPDATE MULTI-PRODUCT ---
            System.out.println("\n=== TEST UPDATE MULTI-PRODUCT ===");
            // Ubah qty: produk1→1, produk2→2
            int newQty1 = 1, newQty2 = 2;
            double newJumlah1 = price1 * newQty1;
            double newJumlah2 = price2 * newQty2;

            DetailTransaksi u1 = new DetailTransaksi(
                kodeProd1, "Produk Multi-1", newQty1, newJumlah1, 0.0,0.0,0.0, newJumlah1
            );
            DetailTransaksi u2 = new DetailTransaksi(
                kodeProd2, "Produk Multi-2", newQty2, newJumlah2, 0.0,0.0,0.0, newJumlah2
            );
            List<DetailTransaksi> updList = new ArrayList<>();
            updList.add(u1);
            updList.add(u2);

            trx.setDetailList(updList);
            trx.setTotal(newJumlah1 + newJumlah2);

            boolean okUpdate = trxDao.updateTransaksi(trx);
            int stock1Upd = getStock(kodeProd1);
            int stock2Upd = getStock(kodeProd2);
            System.out.printf("Update returned: %b | stok1: %d→%d, stok2: %d→%d%n",
                okUpdate,
                stock1After, stock1Upd,
                stock2After, stock2Upd
            );

            // Hitung expected: stok kembali persegi satu: stock1Before - newQty1
            if (!okUpdate
             || stock1Upd != stock1Before - newQty1
             || stock2Upd != stock2Before - newQty2) {
                throw new RuntimeException("Update multi-product / stok mismatch!");
            }

            // Verifikasi fetch update
            FullTransaksi fetched2 = trxDao.getFullTransaksiByNoInv(noInvMulti);
            if (fetched2 == null
             || fetched2.getDetailList().get(0).getQty() != newQty1
             || fetched2.getDetailList().get(1).getQty() != newQty2) {
                throw new RuntimeException("Fetch after update multi-product gagal!");
            }
            System.out.println("Fetch after update multi-product: OK");

            // --- TEST DELETE MULTI-PRODUCT ---
            System.out.println("\n=== TEST DELETE MULTI-PRODUCT ===");
            boolean okDelete = trxDao.deleteTransaksi(noInvMulti);
            int stock1Del = getStock(kodeProd1);
            int stock2Del = getStock(kodeProd2);
            System.out.printf("Delete returned: %b | stok1: %d→%d, stok2: %d→%d%n",
                okDelete,
                stock1Upd, stock1Del,
                stock2Upd, stock2Del
            );

            if (!okDelete
             || stock1Del != stock1Before
             || stock2Del != stock2Before) {
                throw new RuntimeException("Delete multi-product / stok mismatch!");
            }
            System.out.println("Fetch after delete multi-product: " +
                (trxDao.getFullTransaksiByNoInv(noInvMulti)==null ? "OK" : "GAGAL"));

            System.out.println("\nALL MULTI-PRODUCT TESTS PASSED 🎉");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("TEST MULTI-PRODUCT FAILED: " + e.getMessage());
        } finally {
            // CLEANUP: hapus data master
            System.out.println("\n=== CLEANUP: DELETE PRODUK & CUSTOMER ===");
            try {
                new TransaksiDao().deleteTransaksi(noInvMulti);
            } catch (Exception ignored) {}
            if (produkDao.delete(kodeProd1))  System.out.println("Deleted produk: " + kodeProd1);
            if (produkDao.delete(kodeProd2))  System.out.println("Deleted produk: " + kodeProd2);
            if (customerDao.delete(kodeCust)) System.out.println("Deleted customer: " + kodeCust);
        }
    }
}
