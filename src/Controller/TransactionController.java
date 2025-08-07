/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Dao.TransaksiDao;
import Model.FullTransaksi;
import Model.Transaksi;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author andid
 */
public class TransactionController {
    private final TransaksiDao dao;

    public TransactionController() {
        this.dao = new TransaksiDao();
    }

    /**
     * Mengambil daftar semua transaksi (header) lengkap.
     */
    public List<FullTransaksi> getAllFullTransaksi() {
        return dao.getFullTransaksiList();
    }
    
    /** Hapus transaksi berdasarkan no invoice. */
    public boolean deleteTransaksi(String noInv) {
        return dao.deleteTransaksi(noInv);
    }

    /** Generate nomor invoice baru. */
    public String generateInvoiceNo() {
        try {
            return dao.generateInvoiceNo();
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
    }

    /** Ambil satu FullTransaksi untuk edit. */
    public FullTransaksi getFullTransaksi(String noInv) {
        return dao.getFullTransaksiByNoInv(noInv);
    }

    /** Simpan transaksi baru (insert header+detail). */
    public boolean saveTransaksi(Transaksi trx) {
        return dao.insertTransaksi(trx);
    }

    /** Update transaksi (header+detail). */
    public boolean updateTransaksi(Transaksi trx) {
        return dao.updateTransaksi(trx);
    }
}
