/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.sql.Date;
import java.util.List;

/**
 *
 * @author andid
 */
public class FullTransaksi {
    private String noInv;
    private String kodeCust;
    private String namaCust;
    private String alamatCust;
    private Date tglInv;
    private double total;
    private List<DetailTransaksi> detailList;

    public FullTransaksi(String noInv, String kodeCust, String namaCust, String alamatCust, Date tglInv, double total, List<DetailTransaksi> detailList) {
        this.noInv = noInv;
        this.kodeCust = kodeCust;
        this.namaCust = namaCust;
        this.alamatCust = alamatCust;
        this.tglInv = tglInv;
        this.total = total;
        this.detailList = detailList;
    }

    public String getNoInv() {
        return noInv;
    }

    public void setNoInv(String noInv) {
        this.noInv = noInv;
    }

    public String getKodeCust() {
        return kodeCust;
    }

    public void setKodeCust(String kodeCust) {
        this.kodeCust = kodeCust;
    }

    public String getNamaCust() {
        return namaCust;
    }

    public void setNamaCust(String namaCust) {
        this.namaCust = namaCust;
    }

    public String getAlamatCust() {
        return alamatCust;
    }

    public void setAlamatCust(String alamatCust) {
        this.alamatCust = alamatCust;
    }

    public Date getTglInv() {
        return tglInv;
    }

    public void setTglInv(Date tglInv) {
        this.tglInv = tglInv;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<DetailTransaksi> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<DetailTransaksi> detailList) {
        this.detailList = detailList;
    }
    
    
}
