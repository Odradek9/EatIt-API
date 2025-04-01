/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eat_it.model;

import jakarta.xml.bind.annotation.XmlRootElement;
import java.time.LocalDate;

/**
 *
 * @author anton
 */
@XmlRootElement
public class Coupon {
    
    private String uid = null;
    private Integer priceCut = null;
    private LocalDate expirationDate = null;
    private Integer customerUid = null;
    private Integer isUsed = null;

    public Coupon() {
    }

    /**
     * @return the uid
     */
    public String getUid() {
        return uid;
    }

    /**
     * @param uid the uid to set
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * @return the priceCut
     */
    public Integer getPriceCut() {
        return priceCut;
    }

    /**
     * @param priceCut the priceCut to set
     */
    public void setPriceCut(Integer priceCut) {
        this.priceCut = priceCut;
    }

    /**
     * @return the expirationDate
     */
    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    /**
     * @param expirationDate the expirationDate to set
     */
    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
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
     * @return the isUsed
     */
    public Integer getIsUsed() {
        return isUsed;
    }

    /**
     * @param isUsed the isUsed to set
     */
    public void setIsUsed(Integer isUsed) {
        this.isUsed = isUsed;
    }

    @Override
    public String toString() {
        return "Coupon{" + "uid=" + uid + ", priceCut=" + priceCut + ", expirationDate=" + expirationDate + ", customerUid=" + customerUid + ", isUsed=" + isUsed + '}';
    }
    
}
