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
public class DetailTransaksi {
    private String kodeProduk;
    private String namaProduk;
    private int qty;
    private double jumlah;
    private double dsc1;
    private double dsc2;
    private double dsc3;
    private double hargaAkhir;

    public DetailTransaksi(String kodeProduk, String namaProduk, int qty, double jumlah, double dsc1, double dsc2, double dsc3, double hargaAkhir) {
        this.kodeProduk = kodeProduk;
        this.namaProduk = namaProduk;
        this.qty = qty;
        this.jumlah = jumlah;
        this.dsc1 = dsc1;
        this.dsc2 = dsc2;
        this.dsc3 = dsc3;
        this.hargaAkhir = hargaAkhir;
    }

    public String getKodeProduk() {
        return kodeProduk;
    }

    public void setKodeProduk(String kodeProduk) {
        this.kodeProduk = kodeProduk;
    }

    public String getNamaProduk() {
        return namaProduk;
    }

    public void setNamaProduk(String namaProduk) {
        this.namaProduk = namaProduk;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getJumlah() {
        return jumlah;
    }

    public void setJumlah(double jumlah) {
        this.jumlah = jumlah;
    }

    public double getDsc1() {
        return dsc1;
    }

    public void setDsc1(double dsc1) {
        this.dsc1 = dsc1;
    }

    public double getDsc2() {
        return dsc2;
    }

    public void setDsc2(double dsc2) {
        this.dsc2 = dsc2;
    }

    public double getDsc3() {
        return dsc3;
    }

    public void setDsc3(double dsc3) {
        this.dsc3 = dsc3;
    }

    public double getHargaAkhir() {
        return hargaAkhir;
    }

    public void setHargaAkhir(double hargaAkhir) {
        this.hargaAkhir = hargaAkhir;
    }

    
    
}
