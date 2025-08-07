/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao.Test;

import Dao.CustomerDao;
import Model.Customer;
import java.util.List;

/**
 *
 * @author andid
 */
public class CustomerDaoTest {
    public static void main(String[] args) {
        CustomerDao dao = new CustomerDao();

        // 1. TEST INSERT
        Customer newCust = new Customer(
            "",
            "C001", 
            "Andi Wijaya", 
            "Jl. Merdeka No.1", 
            "Kelurahan X", 
            "Kecamatan Y", 
            "Semarang", 
            "Jawa Tengah", 
            "50232"
        );
        boolean insertResult = dao.insert(newCust);
        System.out.println("Insert C001: " + (insertResult ? "SUKSES" : "GAGAL"));

        // 2. TEST SELECT ALL
        List<Customer> all = dao.getCustomerList();
        System.out.println("\n== Daftar Semua Customer ==");
        for (Customer c : all) {
            System.out.printf("%s | %s | %s\n",
                c.getKodeBaru(), c.getNama(), c.getKota());
        }

        // 3. TEST SELECT BY KODE
        Customer fetched = dao.getCustomerByKode("C001");
        System.out.println("\nGet by Kode C001:");
        if (fetched != null) {
            System.out.println("Nama: " + fetched.getNama());
            System.out.println("Alamat: " + fetched.getAlamatLengkap());
        } else {
            System.out.println("Customer tidak ditemukan.");
        }

        // 4. TEST UPDATE
        if (fetched != null) {
            fetched.setNama("Andi Wijaya Updated");
            boolean updateResult = dao.update(fetched);
            System.out.println("\nUpdate C001: " + (updateResult ? "SUKSES" : "GAGAL"));

            // Verifikasi update
            Customer updated = dao.getCustomerByKode("C001");
            System.out.println("Nama setelah update: " + updated.getNama());
        }

        // 5. TEST DELETE
        boolean deleteResult = dao.delete("C001");
        System.out.println("\nDelete C001: " + (deleteResult ? "SUKSES" : "GAGAL"));

        // Verifikasi delete
        Customer shouldBeNull = dao.getCustomerByKode("C001");
        System.out.println("Cari C001 setelah delete: " +
           (shouldBeNull == null ? "Tidak ditemukan (OK)" : "Masih ada!"));
    }
}
