/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eat_it.model;

import jakarta.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author anton
 */
@XmlRootElement
public class Review {
    
    private Integer uid = null;
    private Integer stars = null;
    private String comment = null;
    private Integer customerUid = null;

    public Review() {
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
     * @return the stars
     */
    public Integer getStars() {
        return stars;
    }

    /**
     * @param stars the stars to set
     */
    public void setStars(Integer stars) {
        this.stars = stars;
    }

    /**
     * @return the comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param comment the comment to set
     */
    public void setComment(String comment) {
        this.comment = comment;
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

    @Override
    public String toString() {
        return "Review{" + "uid=" + uid + ", stars=" + stars + ", comment=" + comment + ", customerUid=" + customerUid + '}';
    }
    
    
}
