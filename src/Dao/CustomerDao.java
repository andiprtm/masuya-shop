/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Db.DBConnection;
import Model.Customer;
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
public class CustomerDao {
    
    public CustomerDao(){
        
    }
    // INSERT
    public boolean insert(Customer customer) {
        String sql = "EXEC sp_InsertCustomer ?, ?, ?, ?, ?, ?, ?, ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, customer.getKodeBaru());
            stmt.setString(2, customer.getNama());
            stmt.setString(3, customer.getAlamatLengkap());
            stmt.setString(4, customer.getKelurahan());
            stmt.setString(5, customer.getKecamatan());
            stmt.setString(6, customer.getKota());
            stmt.setString(7, customer.getProvinsi());
            stmt.setString(8, customer.getKodePos());

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Insert failed: " + e.getMessage());
            return false;
        }
    }

    // UPDATE
    public boolean update(Customer customer) {
        String sql = "EXEC sp_UpdateCustomer ?, ?, ?, ?, ?, ?, ?, ?, ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, customer.getKodeLama());
            stmt.setString(2, customer.getKodeBaru());
            stmt.setString(3, customer.getNama());
            stmt.setString(4, customer.getAlamatLengkap());
            stmt.setString(5, customer.getKelurahan());
            stmt.setString(6, customer.getKecamatan());
            stmt.setString(7, customer.getKota());
            stmt.setString(8, customer.getProvinsi());
            stmt.setString(9, customer.getKodePos());

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Update failed: " + e.getMessage());
            return false;
        }
    }

    // DELETE
    public boolean delete(String kode) {
        String sql = "EXEC sp_DeleteCustomer ?";
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

    // SELECT ALL or by Keyword
    public List<Customer> getCustomerList() {
        List<Customer> list = new ArrayList<>();
        String sql = "EXEC sp_SelectCustomer";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Customer c = new Customer(
                    rs.getString("kode"),
                    rs.getString("kode"),
                    rs.getString("nama"),
                    rs.getString("alamat_lengkap"),
                    rs.getString("kelurahan"),
                    rs.getString("kecamatan"),
                    rs.getString("kota"),
                    rs.getString("provinsi"),
                    rs.getString("kode_pos")
                );
                list.add(c);
            }

            rs.close();
        } catch (SQLException e) {
            System.err.println("Select failed: " + e.getMessage());
        }

        return list;
    }

    // SELECT by kode
    public Customer getCustomerByKode(String kode) {
        String sql = "EXEC sp_SelectCustomerByKode ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, kode);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Customer(
                    rs.getString("kode"),
                    rs.getString("kode"),
                    rs.getString("nama"),
                    rs.getString("alamat_lengkap"),
                    rs.getString("kelurahan"),
                    rs.getString("kecamatan"),
                    rs.getString("kota"),
                    rs.getString("provinsi"),
                    rs.getString("kode_pos")
                );
            }

            rs.close();
        } catch (SQLException e) {
            System.err.println("Get by kode failed: " + e.getMessage());
        }

        return null;
    }
    
    public Customer getCustomerByKeyword(String keyword) {
        String sql = "EXEC sp_SearchCustomer ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, keyword);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Customer(
                    rs.getString("kode"),
                    rs.getString("kode"),
                    rs.getString("nama"),
                    rs.getString("alamat_lengkap"),
                    rs.getString("kelurahan"),
                    rs.getString("kecamatan"),
                    rs.getString("kota"),
                    rs.getString("provinsi"),
                    rs.getString("kode_pos")
                );
            }

            rs.close();
        } catch (SQLException e) {
            System.err.println("Get by keyowrd failed: " + e.getMessage());
        }

        return null;
    }
}
