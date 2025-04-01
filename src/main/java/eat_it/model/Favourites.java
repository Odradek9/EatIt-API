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
public class Favourites {
    
    private Integer uid = null;
    private ArrayList<Product> products = null;

    public Favourites() {
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
     * @return the products
     */
    public ArrayList<Product> getProducts() {
        return products;
    }

    /**
     * @param products the products to set
     */
    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "Favourites{" + "uid=" + uid + '}';
    }
    
    
}
