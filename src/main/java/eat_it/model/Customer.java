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
public class Customer extends User{
    
    private String surname = null;
    private Integer cartUid = null;
    private Integer favUid = null;
    private ArrayList<CreditCard> creditCards = new ArrayList<>();
    private ArrayList<Order> orders = new ArrayList<>();
    private ArrayList<Coupon> coupons = new ArrayList<>();
    private String imagePath = null;
    private Float total = null;

    public Customer() {
    }

    /**
     * @return the surname
     */
    public String getSurname() {
        return surname;
    }

    /**
     * @param surname the surname to set
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * @return the cartUid
     */
    public Integer getCartUid() {
        return cartUid;
    }

    /**
     * @param cartUid the cartUid to set
     */
    public void setCartUid(Integer cartUid) {
        this.cartUid = cartUid;
    }

    /**
     * @return the favUid
     */
    public Integer getFavUid() {
        return favUid;
    }

    /**
     * @param favUid the favUid to set
     */
    public void setFavUid(Integer favUid) {
        this.favUid = favUid;
    }

    /**
     * @return the creditCards
     */
    public ArrayList<CreditCard> getCreditCards() {
        return creditCards;
    }

    /**
     * @param creditCards the creditCards to set
     */
    public void setCreditCards(ArrayList<CreditCard> creditCards) {
        this.creditCards = creditCards;
    }

    /**
     * @return the orders
     */
    public ArrayList<Order> getOrders() {
        return orders;
    }

    /**
     * @param orders the orders to set
     */
    public void setOrders(ArrayList<Order> orders) {
        this.orders = orders;
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

    /**
     * @return the imagePath
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * @param imagePath the imagePath to set
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    /**
     * @return the total
     */
    public Float getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(Float total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "Customer{" + "surname=" + surname + ", cartUid=" + cartUid + ", favUid=" + favUid + ", imagePath=" + imagePath + '}';
    }
    
}
