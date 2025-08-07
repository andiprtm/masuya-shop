/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author andid
 */
public class Customer {
    private String kodeLama;
    private String kodeBaru;
    private String nama;
    private String alamatLengkap;
    private String kelurahan;
    private String kecamatan;
    private String kota;
    private String provinsi;
    private String kodePos;

    public Customer(String kodeLama, String kodeBaru, String nama, String alamatLengkap, String kelurahan, String kecamatan, String kota, String provinsi, String kodePos) {
        this.kodeLama = kodeLama;
        this.kodeBaru = kodeBaru;
        this.nama = nama;
        this.alamatLengkap = alamatLengkap;
        this.kelurahan = kelurahan;
        this.kecamatan = kecamatan;
        this.kota = kota;
        this.provinsi = provinsi;
        this.kodePos = kodePos;
    }

    public String getKodeLama() {
        return kodeLama;
    }

    public void setKodeLama(String kodeLama) {
        this.kodeLama = kodeLama;
    }

    public String getKodeBaru() {
        return kodeBaru;
    }

    public void setKodeBaru(String kodeBaru) {
        this.kodeBaru = kodeBaru;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamatLengkap() {
        return alamatLengkap;
    }

    public void setAlamatLengkap(String alamatLengkap) {
        this.alamatLengkap = alamatLengkap;
    }

    public String getKelurahan() {
        return kelurahan;
    }

    public void setKelurahan(String kelurahan) {
        this.kelurahan = kelurahan;
    }

    public String getKecamatan() {
        return kecamatan;
    }

    public void setKecamatan(String kecamatan) {
        this.kecamatan = kecamatan;
    }

    public String getKota() {
        return kota;
    }

    public void setKota(String kota) {
        this.kota = kota;
    }

    public String getProvinsi() {
        return provinsi;
    }

    public void setProvinsi(String provinsi) {
        this.provinsi = provinsi;
    }

    public String getKodePos() {
        return kodePos;
    }

    public void setKodePos(String kodePos) {
        this.kodePos = kodePos;
    }

    
    
    
}
