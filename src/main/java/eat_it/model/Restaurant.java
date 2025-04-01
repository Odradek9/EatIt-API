/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eat_it.model;

import jakarta.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 *
 * @author anton
 */
@XmlRootElement
public class Restaurant extends User{
    
    private BigDecimal phone = null;
    private ArrayList<Category> categories = null;
    private ArrayList<Coupon> coupons = null;

    public Restaurant() {
    }


    /**
     * @return the categories
     */
    public ArrayList<Category> getCategories() {
        return categories;
    }

    /**
     * @param categories the categories to set
     */
    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    /**
     * @return the coupons
     */
    public ArrayList<Coupon> getCoupons() {
        return coupons;
    }

    /**
     * @param coupons the coupons to set
     */
    public void setCoupons(ArrayList<Coupon> coupons) {
        this.coupons = coupons;
    }

    @Override
    public String toString() {
        return "Restaurant{" + "phone=" + getPhone() + '}';
    }

    /**
     * @return the phone
     */
    public BigDecimal getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(BigDecimal phone) {
        this.phone = phone;
    }
    
}
