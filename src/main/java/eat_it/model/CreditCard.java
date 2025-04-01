/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eat_it.model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.time.LocalDate;

/**
 *
 * @author anton
 */
@XmlRootElement
public class CreditCard {
    
    @XmlElement
    private Integer uid = null;
    @XmlElement
    private Integer cardNumber = null;
    @XmlElement
    private Integer cvv = null;
    @XmlElement
    private String cardHolder = null;
    @XmlElement
    private LocalDate expirationDate = null;

    public CreditCard() {
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
     * @return the cardNumber
     */
    public Integer getNumber() {
        return cardNumber;
    }

    /**
     * @param number the cardNumber to set
     */
    public void setNumber(Integer number) {
        this.cardNumber = number;
    }

    /**
     * @return the cvv
     */
    public Integer getCvv() {
        return cvv;
    }

    /**
     * @param cvv the cvv to set
     */
    public void setCvv(Integer cvv) {
        this.cvv = cvv;
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
     * @param cardHolder the cardHolder to set
     */
    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    /**
     * @return the cardHolder
     */
    public String getCardHolder() {
        return cardHolder;
    }

    @Override
    public String toString() {
        return "CreditCard{" + "uid=" + uid + ", cardNumber=" + cardNumber + ", cvv=" + cvv + ", cardHolder=" + cardHolder + ", expirationDate=" + expirationDate + '}';
    }
}
