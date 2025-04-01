/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eat_it.model;

import java.net.URI;
import java.util.Map;

/**
 *
 * @author anton
 */
public class Statistics {
    
    private float montlyRevenues = 0;
    private URI bestSeller = null;
    private URI worstSeller = null;
    private Map<String, Integer> monthlyOrders = null;

    /**
     * @return the montlyRevenues
     */
    public float getMontlyRevenues() {
        return montlyRevenues;
    }

    /**
     * @param montlyRevenues the montlyRevenues to set
     */
    public void setMontlyRevenues(float montlyRevenues) {
        this.montlyRevenues = montlyRevenues;
    }

    

    /**
     * @return the monthlyOrders
     */
    public Map<String, Integer> getMonthlyOrders() {
        return monthlyOrders;
    }

    /**
     * @param monthlyOrders the monthlyOrders to set
     */
    public void setMonthlyOrders(Map<String, Integer> monthlyOrders) {
        this.monthlyOrders = monthlyOrders;
    }

    /**
     * @return the bestSeller
     */
    public URI getBestSeller() {
        return bestSeller;
    }

    /**
     * @param bestSeller the bestSeller to set
     */
    public void setBestSeller(URI bestSeller) {
        this.bestSeller = bestSeller;
    }

    /**
     * @return the worstSeller
     */
    public URI getWorstSeller() {
        return worstSeller;
    }

    /**
     * @param worstSeller the worstSeller to set
     */
    public void setWorstSeller(URI worstSeller) {
        this.worstSeller = worstSeller;
    }

    @Override
    public String toString() {
        return "Statistics{" + "montlyRevenues=" + montlyRevenues + ", bestSeller=" + bestSeller + ", worstSeller=" + worstSeller + ", monthlyOrders=" + monthlyOrders + '}';
    }
    
}
