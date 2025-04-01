/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eat_it.model;

import jakarta.xml.bind.annotation.XmlRootElement;
import java.net.URI;

/**
 *
 * @author anton
 */
@XmlRootElement
public class JsonData {
    
    private URI productURI = null;
    private Integer productUid = null;
    private Integer quantity = null;
    private String couponUid = null;
    private Integer cardUid = null;
    private Integer addressUid = null;

    

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
     * @return the cardUid
     */
    public Integer getCardUid() {
        return cardUid;
    }

    /**
     * @param cardUid the cardUid to set
     */
    public void setCardUid(Integer cardUid) {
        this.cardUid = cardUid;
    }

    /**
     * @return the quantity
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the productURI
     */
    public URI getProductURI() {
        return productURI;
    }

    /**
     * @param productURI the productURI to set
     */
    public void setProductURI(URI productURI) {
        this.productURI = productURI;
    }

    /**
     * @return the productUid
     */
    public Integer getProductUid() {
        return productUid;
    }

    /**
     * @param productUid the productUid to set
     */
    public void setProductUid(Integer productUid) {
        this.productUid = productUid;
    }

    /**
     * @return the couponUid
     */
    public String getCouponUid() {
        return couponUid;
    }

    /**
     * @param couponUid the couponUid to set
     */
    public void setCouponUid(String couponUid) {
        this.couponUid = couponUid;
    }
    
}
