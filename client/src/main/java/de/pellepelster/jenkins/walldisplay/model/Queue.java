/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.pellepelster.jenkins.walldisplay.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author U299468
 */
public class Queue {

    private List<Item> items = new ArrayList<Item>();

    /**
     * @return the items
     */
    public List<Item> getItems() {
        return items;
    }

    /**
     * @param items the items to set
     */
    public void setItems(List<Item> items) {
        this.items = items;
    }
    
}
