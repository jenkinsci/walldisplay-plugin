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
