/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author andid
 */
public class Helper {
     /**
     * Format java.util.Date atau java.sql.Date ke string "dd-MM-yyyy".
     *
     * @param date objek Date (java.util.Date atau java.sql.Date)
     * @return string tanggal dalam format dd-MM-yyyy, atau empty string jika date null
     */
    public static String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(date);
    }

    /**
     * Format numeric ke mata uang Rupiah (IDR), misal 10000 -> "Rp10.000,00".
     *
     * @param amount nilai numerik
     * @return string formatted Rupiah sesuai locale Indonesia
     */
    public static String formatRupiah(double amount) {
        Locale localeID = new Locale("id", "ID");
        NumberFormat nf = NumberFormat.getCurrencyInstance(localeID);
        return nf.format(amount);
    }
}
