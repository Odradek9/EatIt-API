/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eat_it.model;

import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

/**
 *
 * @author anton
 */
@XmlRootElement
public class Cart {
    
    private Integer uid = null;

    @Override
    public String toString() {
        return "Cart{" + "uid=" + uid + ", Products=" + Products + '}';
    }
    private ArrayList<Product> Products = new ArrayList<>();

    public Cart() {
    }
    
    /**
     * @return the uid
     */
    public Integer getUid() {
        return uid;
    }

    /**
     * @param uid the uid to set
     */
    public void setUid(Integer uid) {
        this.uid = uid;
    }

    /**
     * @return the Products
     */
    public ArrayList<Product> getProducts() {
        return Products;
    }

    /**
     * @param Products the Products to set
     */
    public void setProducts(ArrayList<Product> Products) {
        this.Products = Products;
    }
    
}
