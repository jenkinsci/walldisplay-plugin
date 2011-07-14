package de.pellepelster.jenkins.walldisplay.model;

import java.util.ArrayList;
import java.util.List;

/**
 * XStream object for jenkins views
 * 
 * @author pelle
 */
public class View {

    private String name;
    private List<BaseProject> jobs = new ArrayList<BaseProject>();

    private View()
    {
        
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the jobs
     */
    public List<BaseProject> getJobs() {
        return jobs;
    }

    /**
     * @param jobs to set
     */
    public void setJobs(List<BaseProject> jobs) {
        this.jobs = jobs;
    }
}
