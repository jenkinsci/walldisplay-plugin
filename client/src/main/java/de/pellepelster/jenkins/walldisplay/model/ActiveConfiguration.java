package de.pellepelster.jenkins.walldisplay.model;

/**
 * XStram object for matrix projects active configurations
 * 
 * @author pelle
 */
public class ActiveConfiguration {

    private String url;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
}
