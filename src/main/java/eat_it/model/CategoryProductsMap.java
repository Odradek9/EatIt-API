/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eat_it.model;

import java.net.URI;
import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author anton
 */
public class CategoryProductsMap {
    
    private Map<URI, ArrayList<URI>> map = null;

    /**
     * @return the map
     */
    public Map<URI, ArrayList<URI>> getMap() {
        return map;
    }

    /**
     * @param map the map to set
     */
    public void setMap(Map<URI, ArrayList<URI>> map) {
        this.map = map;
    }

    
    
}
