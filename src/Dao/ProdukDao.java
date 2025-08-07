/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Db.DBConnection;
import Model.Produk;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author andid
 */
public class ProdukDao {

    public ProdukDao() {
       
    }

    // INSERT
    public boolean insert(Produk produk) {
        String sql = "EXEC sp_InsertProduk ?, ?, ?, ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, produk.getKode());
            stmt.setString(2, produk.getNama());
            stmt.setDouble(3, produk.getHarga());
            stmt.setInt(4, produk.getStok());

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Insert failed: " + e.getMessage());
            return false;
        }
    }

    // UPDATE
    public boolean update(Produk produk) {
        String sql = "EXEC sp_UpdateProduk ?, ?, ?, ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, produk.getKode());
            stmt.setString(2, produk.getNama());
            stmt.setDouble(3, produk.getHarga());
            stmt.setInt(4, produk.getStok());

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Update failed: " + e.getMessage());
            return false;
        }
    }

    // DELETE
    public boolean delete(String kode) {
        String sql = "EXEC sp_DeleteProduk ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, kode);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Delete failed: " + e.getMessage());
            return false;
        }
    }

    // SELECT
    public List<Produk> getProdukList(String keyword) {
        List<Produk> list = new ArrayList<>();
        String sql = "EXEC sp_SelectProduk ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, keyword); // bisa null / kosong
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Produk p = new Produk(
                        rs.getString("kode"),
                        rs.getString("nama"),
                        rs.getDouble("harga"),
                        rs.getInt("stok")
                    );
                    list.add(p);
                }
            }
        } catch (SQLException e) {
            System.err.println("Select failed: " + e.getMessage());
        }
        return list;
    }
}
