package de.pellepelster.jenkins.walldisplay.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * XStream objects for the build queue
 * 
 * @author pelle
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
     * @param items to set
     */
    public void setItems(List<Item> items) {
        this.items = items;
    }
    
}
