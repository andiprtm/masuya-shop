/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Dao.ProdukDao;
import Model.Produk;
import Util.OperationResult;
import java.util.List;

/**
 *
 * @author andid
 */
public class ProductController {
    private final ProdukDao produkDao;

    public ProductController() {
        this.produkDao = new ProdukDao();
    }

    /**
     * Fetch all products (no keyword filter).
     */
    public List<Produk> getAllProduk() {
        return produkDao.getProdukList("");
    }

    /**
     * Fetch products matching the given keyword.
     */
    public List<Produk> searchProduk(String keyword) {
        return produkDao.getProdukList(keyword);
    }
    
    /**
     * Insert produk baru.
     */
    public OperationResult insertProduk(Produk produk) {
        boolean ok = produkDao.insert(produk);
        if (ok) {
            return OperationResult.ok(
                String.format("Insert produk %s berhasil", produk.getKode())
            );
        } else {
            return OperationResult.fail(
                String.format("Insert produk %s gagal", produk.getKode())
            );
        }
    }

    /**
     * Update produk yang sudah ada.
     */
    public OperationResult updateProduk(Produk produk) {
        boolean ok = produkDao.update(produk);
        if (ok) {
            return OperationResult.ok(
                String.format("Update produk %s berhasil", produk.getKode())
            );
        } else {
            return OperationResult.fail(
                String.format("Update produk %s gagal", produk.getKode())
            );
        }
    }
    
    /**
     * Hapus produk berdasarkan kode.
     * @param kodeProduk kode produk yang dihapus
     * @return true jika berhasil
     */
    public OperationResult deleteProduk(String kodeProduk) {
        if(produkDao.delete(kodeProduk)){
            return OperationResult.ok(String.format("Hapus produk %s berhasil", kodeProduk));
        } else {
            return OperationResult.fail(String.format("Hapus produk %s gagal", kodeProduk));
        }
    }

    /**
     * Ambil satu produk berdasarkan kode.
     * @param kodeProduk kode produk
     * @return objek Produk atau null jika tidak ditemukan
     */
    public Produk getProdukByKode(String kodeProduk) {
        List<Produk> list = produkDao.getProdukList(kodeProduk);
        return list.isEmpty() ? null : list.get(0);
    }
}
