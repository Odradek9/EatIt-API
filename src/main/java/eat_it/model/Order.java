/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eat_it.model;

import jakarta.xml.bind.annotation.XmlRootElement;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 *
 * @author anton
 */
@XmlRootElement
public class Order {
    
    private Integer uid = null;
    private LocalDate creationDate = null;
    private Float total = null;
    private LocalTime arrivalTime = null;
    private Integer paymentUid = null;
    private Integer couponUid = null;
    private Integer customerUid = null;
    private String state = null;
    private Integer addressUid = null;
    private ArrayList<Product> products = null;

    public Order() {
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
     * @return the creationDate
     */
    public LocalDate getCreationDate() {
        return creationDate;
    }

    /**
     * @param creationDate the creationDate to set
     */
    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
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

    

    /**
     * @return the paymentUid
     */
    public Integer getPaymentUid() {
        return paymentUid;
    }

    /**
     * @param paymentUid the paymentUid to set
     */
    public void setPaymentUid(Integer paymentUid) {
        this.paymentUid = paymentUid;
    }

    /**
     * @return the couponUid
     */
    public Integer getCouponUid() {
        return couponUid;
    }

    /**
     * @param couponUid the couponUid to set
     */
    public void setCouponUid(Integer couponUid) {
        this.couponUid = couponUid;
    }

    /**
     * @return the customerUid
     */
    public Integer getCustomerUid() {
        return customerUid;
    }

    /**
     * @param customerUid the customerUid to set
     */
    public void setCustomerUid(Integer customerUid) {
        this.customerUid = customerUid;
    }

    /**
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * @return the addressUid
     */
    public Integer getAddressUid() {
        return addressUid;
    }

    /**
     * @param addressUid the addressUid to set
     */
    public void setAddressUid(Integer addressUid) {
        this.addressUid = addressUid;
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
        return "Order{" + "uid=" + uid + ", creationDate=" + creationDate + ", total=" + total + ", arrivalTime=" + getArrivalTime() + ", paymentUid=" + paymentUid + ", couponUid=" + couponUid + ", customerUid=" + customerUid + ", state=" + state + ", addressUid=" + addressUid + '}';
    }

    /**
     * @return the arrivalTime
     */
    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

    /**
     * @param arrivalTime the arrivalTime to set
     */
    public void setArrivalTime(LocalTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    
    
}
