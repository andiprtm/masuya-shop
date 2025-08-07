/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Db.DBConnection;
import Model.DetailTransaksi;
import Model.FullTransaksi;
import Model.Transaksi;
import com.microsoft.sqlserver.jdbc.SQLServerCallableStatement;
import com.microsoft.sqlserver.jdbc.SQLServerDataTable;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author andid
 */
public class TransaksiDao {

    /**
     * Insert header + detail transaction using TVP (DetailTransaksiType).
     */
    public boolean insertTransaksi(Transaksi trx) {
        // Sintaks JDBC untuk memanggil SP dengan 5 parameter
        String sql = "{call sp_InsertTransaksi(?, ?, ?, ?, ?)}";

        try (Connection conn = DBConnection.getConnection();
                // 1) prepareCall, bukan prepareStatement
                CallableStatement cs = conn.prepareCall(sql)) {

            // 2) cast ke SQLServerCallableStatement agar method setStructured tersedia
            SQLServerCallableStatement stmt = (SQLServerCallableStatement) cs;

            // 3) bangun TVP sesuai definisi dbo.DetailTransaksiType
            SQLServerDataTable tvp = new SQLServerDataTable();
            tvp.addColumnMetadata("kode_produk", java.sql.Types.VARCHAR);
            tvp.addColumnMetadata("nama_produk", java.sql.Types.VARCHAR);
            tvp.addColumnMetadata("qty", java.sql.Types.INTEGER);
            tvp.addColumnMetadata("jumlah", java.sql.Types.DECIMAL);
            tvp.addColumnMetadata("dsc_1", java.sql.Types.DECIMAL);
            tvp.addColumnMetadata("dsc_2", java.sql.Types.DECIMAL);
            tvp.addColumnMetadata("dsc_3", java.sql.Types.DECIMAL);
            tvp.addColumnMetadata("harga_akhir", java.sql.Types.DECIMAL);

            for (DetailTransaksi d : trx.getDetailList()) {
                tvp.addRow(
                        d.getKodeProduk(),
                        d.getNamaProduk(),
                        d.getQty(),
                        BigDecimal.valueOf(d.getJumlah()),
                        BigDecimal.valueOf(d.getDsc1()),
                        BigDecimal.valueOf(d.getDsc2()),
                        BigDecimal.valueOf(d.getDsc3()),
                        BigDecimal.valueOf(d.getHargaAkhir())
                );
            }

            // 4) set header parameters (1–4)
            stmt.setString(1, trx.getNoInv());
            stmt.setString(2, trx.getKodeCust());
            stmt.setDate(3, trx.getTglInv());
            stmt.setBigDecimal(4, BigDecimal.valueOf(trx.getTotal()));

            // 5) set TVP pada parameter ke-5
            stmt.setStructured(5, "dbo.DetailTransaksiType", tvp);

            // 6) eksekusi
            stmt.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Update header + detail transaction: delete old detail and re-insert.
     */
    public boolean updateTransaksi(Transaksi trx) {
        // 1) Gunakan call escape syntax dengan braces dan (
        String sql = "{call sp_UpdateTransaksi(?, ?, ?, ?, ?)}";

        try (Connection conn = DBConnection.getConnection();
                CallableStatement cs = conn.prepareCall(sql)) {

            // 2) Cast ke implementation agar tersedia setStructured
            SQLServerCallableStatement stmt = (SQLServerCallableStatement) cs;

            // 3) Bangun TVP dengan tepat 8 kolom
            SQLServerDataTable tvp = new SQLServerDataTable();
            tvp.addColumnMetadata("kode_produk", java.sql.Types.VARCHAR);
            tvp.addColumnMetadata("nama_produk", java.sql.Types.VARCHAR);
            tvp.addColumnMetadata("qty", java.sql.Types.INTEGER);
            tvp.addColumnMetadata("jumlah", java.sql.Types.DECIMAL);
            tvp.addColumnMetadata("dsc_1", java.sql.Types.DECIMAL);
            tvp.addColumnMetadata("dsc_2", java.sql.Types.DECIMAL);
            tvp.addColumnMetadata("dsc_3", java.sql.Types.DECIMAL);
            tvp.addColumnMetadata("harga_akhir", java.sql.Types.DECIMAL);

            // 4) Tambahkan satu baris per detail; pastikan 8 nilai → 8 metadata
            for (DetailTransaksi d : trx.getDetailList()) {
                tvp.addRow(
                        d.getKodeProduk(),
                        d.getNamaProduk(),
                        d.getQty(),
                        BigDecimal.valueOf(d.getJumlah()),
                        BigDecimal.valueOf(d.getDsc1()),
                        BigDecimal.valueOf(d.getDsc2()),
                        BigDecimal.valueOf(d.getDsc3()),
                        BigDecimal.valueOf(d.getHargaAkhir())
                );
            }

            // 5) Set header params
            stmt.setString(1, trx.getNoInv());
            stmt.setString(2, trx.getKodeCust());
            stmt.setDate(3, trx.getTglInv());
            stmt.setBigDecimal(4, BigDecimal.valueOf(trx.getTotal()));

            // 6) Kirim TVP sebagai param ke-5
            stmt.setStructured(5, "dbo.DetailTransaksiType", tvp);

            // 7) Eksekusi
            stmt.execute();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Ambil daftar transaksi (header saja) menggunakan
     * sp_SelectFullTransaksiByNoInv.
     */
    public List<FullTransaksi> getFullTransaksiList() {
        List<FullTransaksi> list = new ArrayList<>();
        String sql = "{call sp_SelectFullTransaksiByNoInv}";  // SP dengan default LIKE '%' atau parameter kosong untuk semua

        try (Connection conn = DBConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {

            FullTransaksi current = null;
            String lastNoInv = null;

            while (rs.next()) {
                String noInv = rs.getString("no_inv");
                // Jika encounter transaksi baru, buat objek header baru
                if (!noInv.equals(lastNoInv)) {
                    List<DetailTransaksi> details = new ArrayList<>();

                    String kodeCust   = rs.getString("kode_customer");
                    String namaCust   = rs.getString("nama_customer");
                    // concat alamat
                    String alamatCust = String.format(
                        "%s, Des. %s, Kec. %s, Kota %s, %s %s",
                        rs.getString("alamat_lengkap"),
                        rs.getString("kelurahan"),
                        rs.getString("kecamatan"),
                        rs.getString("kota"),
                        rs.getString("provinsi"),
                        rs.getString("kode_pos")
                    );

                    current = new FullTransaksi(
                        noInv,
                        kodeCust,
                        namaCust,
                        alamatCust,
                        rs.getDate("tgl_inv"),
                        rs.getBigDecimal("total").doubleValue(),
                        details
                    );
                    list.add(current);
                    lastNoInv = noInv;
                }

                // Tambah detail untuk transaksi yang sama
                DetailTransaksi dt = new DetailTransaksi(
                    rs.getString("kode_produk"),
                    rs.getString("nama_produk"),
                    rs.getInt("qty"),
                    rs.getBigDecimal("jumlah").doubleValue(),
                    rs.getDouble("dsc_1"),
                    rs.getDouble("dsc_2"),
                    rs.getDouble("dsc_3"),
                    rs.getDouble("harga_akhir")
                );
                current.getDetailList().add(dt);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Ambil seluruh detail transaksi (+ header + customer + produk info) by
     * noInv.
     */
    public FullTransaksi getFullTransaksiByNoInv(String noInv) {
        String sql = "{call sp_SelectFullTransaksiByNoInv(?)}";
        FullTransaksi trx = null;
        List<DetailTransaksi> details = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
                CallableStatement stmt = conn.prepareCall(sql)) {

            // set param
            stmt.setString(1, noInv);

            // eksekusi dan baca result set
            try (ResultSet rs = stmt.executeQuery()) {
                String kodeCust = null;
                Date tglInv = null;
                String namaCust = null;
                String alamatCust = null;
                double total = 0;

                while (rs.next()) {
                    if (trx == null) {
                        kodeCust = rs.getString("kode_customer");
                        tglInv = rs.getDate("tgl_inv");
                        namaCust = rs.getString("nama_customer");
                        alamatCust = String.format(
                                "%s, Des. %s, Kec. %s, Kota %s, %s %s",
                                rs.getString("alamat_lengkap"),
                                rs.getString("kelurahan"),
                                rs.getString("kecamatan"),
                                rs.getString("kota"),
                                rs.getString("provinsi"),
                                rs.getString("kode_pos")
                        );
                        total = rs.getBigDecimal("total").doubleValue();
                    }
                    details.add(new DetailTransaksi(
                            rs.getString("kode_produk"),
                            rs.getString("nama_produk"),
                            rs.getInt("qty"),
                            rs.getBigDecimal("jumlah").doubleValue(),
                            rs.getBigDecimal("dsc_1").doubleValue(),
                            rs.getBigDecimal("dsc_2").doubleValue(),
                            rs.getBigDecimal("dsc_3").doubleValue(),
                            rs.getBigDecimal("harga_akhir").doubleValue()
                    ));
                }
                if (!details.isEmpty()) {
                    trx = new FullTransaksi(noInv, kodeCust, namaCust, alamatCust, tglInv, total, details);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trx;
    }

    /**
     * Delete transaksi header + detail.
     */
    public boolean deleteTransaksi(String noInv) {
        String sql = "{call sp_DeleteTransaksi(?)}";
        try (Connection conn = DBConnection.getConnection();
                CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setString(1, noInv);
            stmt.execute();
            return true;
        } catch (SQLException e) {
            // jika pesan sama dengan “Nomor invoice tidak ditemukan.”, abaikan
            if (e.getMessage().contains("Nomor invoice tidak ditemukan")) {
                return false;
            } else {
                e.printStackTrace();
                return false;
            }
        }
    }
    
    /**
     * Generate invoice number.
     */
    public String generateInvoiceNo() throws SQLException {
    String sql = "{ call sp_GenerateInvoiceNo(?) }";
    try (Connection conn = DBConnection.getConnection();
         CallableStatement cs = conn.prepareCall(sql)) {

        cs.registerOutParameter(1, java.sql.Types.VARCHAR);
        cs.execute();
        return cs.getString(1);
    }
}
}
