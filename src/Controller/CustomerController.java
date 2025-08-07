/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;
import Dao.CustomerDao;
import Model.Customer;
import java.util.List;
import java.util.stream.Collectors;
/**
 *
 * @author andid
 */
public class CustomerController {
    private final CustomerDao customerDao;

    public CustomerController() {
        this.customerDao = new CustomerDao();
    }

    /**
     * Mengambil semua data customer.
     */
    public List<Customer> getAllCustomers() {
        return customerDao.getCustomerList();
    }

    /**
     * Mengambil satu customer berdasarkan kode.
     */
    public Customer getCustomerByKode(String kode) {
        return customerDao.getCustomerByKode(kode);
    }
    
    /**
     * Hapus customer berdasarkan kode.
     * @param kode kode customer
     * @return true jika berhasil
     */
    public boolean deleteCustomer(String kode) {
        return customerDao.delete(kode);
    }

    /**
     * Simpan atau update Customer: jika kodeLama kosong -> insert, else update.
     * @param c objek Customer
     * @return true jika berhasil
     */
    public boolean saveOrUpdateCustomer(Customer c) {
        if (c.getKodeLama() == null || c.getKodeLama().isEmpty()) {
            return customerDao.insert(c);
        } else {
            return customerDao.update(c);
        }
    }
    
    public List<Customer> searchCustomers(String keyword) {
        String kw = keyword == null ? "" : keyword.toLowerCase();
        return getAllCustomers().stream()
            .filter(c ->
                c.getKodeBaru().toLowerCase().contains(kw)
             || c.getNama().toLowerCase().contains(kw)
            )
            .collect(Collectors.toList());
    }
}
