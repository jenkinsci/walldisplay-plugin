package de.pellepelster.jenkins.walldisplay.model;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 *
 * @author pelle
 */
@XStreamAlias("hudson")
public class Hudson {

    private List<Job> jobs = new ArrayList<Job>();
    private List<View> views = new ArrayList<View>();
    private String nodeDescription;

    /**
     * @return the nodeDescription
     */
    public String getNodeDescription() {
        return nodeDescription;
    }

    /**
     * @param nodeDescription the nodeDescription to set
     */
    public void setNodeDescription(String nodeDescription) {
        this.nodeDescription = nodeDescription;
    }

    /**
     * @return the views
     */
    public List<View> getViews() {
        return views;
    }

    /**
     * @param views the views to set
     */
    public void setViews(List<View> views) {
        this.views = views;
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
