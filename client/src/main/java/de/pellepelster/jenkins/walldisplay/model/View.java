package de.pellepelster.jenkins.walldisplay.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author pelle
 */
public class View {

    private String name;
    private List<Job> jobs = new ArrayList<Job>();

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
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the jobs
     */
    public List<Job> getJobs() {
        return jobs;
    }

    /**
     * @param jobs the jobs to set
     */
    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }
}
