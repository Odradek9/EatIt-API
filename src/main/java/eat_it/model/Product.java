/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eat_it.model;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;

/**
 *
 * @author anton
 */
@XmlRootElement
public class Product {
    
    private Integer uid = null;
    private String name = null;
    private String description = null;
    private Float price = null;
    private String imagePath = null;
    private Integer timesOrdered = null;
    @XmlTransient
    private ArrayList<Review> reviews = null;

    public Product() {
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
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the price
     */
    public Float getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(Float price) {
        this.price = price;
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
     * @return the timesOrdered
     */
    public Integer getTimesOrdered() {
        return timesOrdered;
    }

    /**
     * @param timesOrdered the timesOrdered to set
     */
    public void setTimesOrdered(Integer timesOrdered) {
        this.timesOrdered = timesOrdered;
    }

    /**
     * @return the reviews
     */
    public ArrayList<Review> getReviews() {
        return reviews;
    }

    /**
     * @param reviews the reviews to set
     */
    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    @Override
    public String toString() {
        return "Product{" + "uid=" + uid + ", name=" + name + ", description=" + description + ", price=" + price + ", imagePath=" + imagePath + ", timesOrdered=" + timesOrdered + '}';
    }

    
     
    
    
    
}
