/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao.Test;

import Dao.ProdukDao;
import Model.Produk;
import java.util.List;

/**
 *
 * @author andid
 */
public class ProductDaoTest {
    public static void main(String[] args) {
        ProdukDao dao = new ProdukDao();

        // 1. Test Insert
        Produk produkBaru = new Produk("PRD999", "Test Produk", 5000, 10);
        if (dao.insert(produkBaru)) {
            System.out.println("Insert berhasil");
        } else {
            System.out.println("Insert gagal");
        }

        // 2. Test Update
        produkBaru.setHarga(6000);
        produkBaru.setStok(20);
        if (dao.update(produkBaru)) {
            System.out.println("Update berhasil");
        } else {
            System.out.println("Update gagal");
        }

        // 3. Test Select
        System.out.println("Daftar Produk:");
        List<Produk> list = dao.getProdukList("");  // kosong = ambil semua
        for (Produk p : list) {
            System.out.println(p.getKode() + " | " + p.getNama() + " | " + p.getHarga() + " | " + p.getStok());
        }
        
        // 4. Test Select by Kode
        System.out.println("Daftar Produk kode PRD99:");
        list = dao.getProdukList("PRD99");  // ambil kode
        for (Produk p : list) {
            System.out.println(p.getKode() + " | " + p.getNama() + " | " + p.getHarga() + " | " + p.getStok());
        }

        // 4. Test Delete
        if (dao.delete("PRD999")) {
            System.out.println("Delete berhasil");
        } else {
            System.out.println("Delete gagal");
        }
    }
}
