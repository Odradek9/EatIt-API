/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eat_it.model;

import java.util.ArrayList;

/**
 *
 * @author anton
 */
public class JsonList {
    
    private ArrayList<JsonData> JsonDatalist = null;

    /**
     * @return the list
     */
    public ArrayList<JsonData> getList() {
        return JsonDatalist;
    }

    /**
     * @param list the list to set
     */
    public void setList(ArrayList<JsonData> list) {
        this.JsonDatalist = list;
    }
    
}
