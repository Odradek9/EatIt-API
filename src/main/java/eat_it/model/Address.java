/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eat_it.model;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

/**
 *
 * @author anton
 */
@XmlRootElement
public class Address {
    
    private Integer uid = null;
    private String city = null;
    private Integer cap = null;
    private String street = null;
    private Integer homeNumber = null;
    
    public Address(){
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

    @Override
    public String toString() {
        return "Address{" + "uid=" + uid + ", city=" + city + ", cap=" + cap + ", street=" + street + ", homeNumber=" + homeNumber + '}';
    }

    /**
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return the cap
     */
    public Integer getCap() {
        return cap;
    }

    /**
     * @param cap the cap to set
     */
    public void setCap(Integer cap) {
        this.cap = cap;
    }

    /**
     * @return the street
     */
    public String getStreet() {
        return street;
    }

    /**
     * @param street the street to set
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * @return the homeNumber
     */
    public Integer getHomeNumber() {
        return homeNumber;
    }

    /**
     * @param homeNumber the homeNumber to set
     */
    public void setHomeNumber(Integer homeNumber) {
        this.homeNumber = homeNumber;
    }
    
}
