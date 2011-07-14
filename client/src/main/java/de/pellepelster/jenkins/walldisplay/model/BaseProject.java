package de.pellepelster.jenkins.walldisplay.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Basix XStream object for project properties shared by all projects
 * 
 * @author pelle
 */
public class BaseProject {

    private List<ActiveConfiguration> activeConfigurations = new ArrayList<ActiveConfiguration>();
    private String displayName;
    private String name;
    private String url;
    private String color;
    private Build lastSuccessfulBuild;
    private Build lastBuild;
    private Integer queuePosition;
    private String description;
    private List<JobProperty> jobProperties = new ArrayList<JobProperty>();

    /**
     * @return the color
     */
    public String getColor() {
        return color;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return job properties for this job
     */
    public List<JobProperty> getJobProperties() {
        return jobProperties;
    }

    /**
     * @return the displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @return the last build
     */
    public Build getLastBuild() {
        return lastBuild;
    }

    /**
     * @return the lastSuccessfulBuild
     */
    public Build getLastSuccessfulBuild() {
        return lastSuccessfulBuild;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the queuePosition
     */
    public Integer getQueuePosition() {
        return queuePosition;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param color the color to set
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * @param name the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @param job properties to set
     */
    public void setJobProperties(List<JobProperty> jobProperties) {
        this.jobProperties = jobProperties;
    }

    /**
     * @param displayName to set
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * @param build to set
     */
    public void setLastBuild(Build lastBuild) {
        this.lastBuild = lastBuild;
    }

    /**
     * @param lastSuccessfulBuild to set
     */
    public void setLastSuccessfulBuild(Build lastSuccessfulBuild) {
        this.lastSuccessfulBuild = lastSuccessfulBuild;
    }

    public List<ActiveConfiguration> getActiveConfigurations() {
        return activeConfigurations;
    }

    public void setActiveConfigurations(List<ActiveConfiguration> activeConfigurations) {
        this.activeConfigurations = activeConfigurations;
    }


    /**
     * @param name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param queuePosition to set
     */
    public void setQueuePosition(Integer queuePosition) {
        this.queuePosition = queuePosition;
    }

    /**
     * @param url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Checks a job for the existence of a certain job property
     * 
     * @param name of the job property
     * 
     * @return true if the job property exists
     */
    public boolean hasJobProperty(String propertyName) {
        return getJobProperty(propertyName) != null;
    }

    /**
     * Returns a certain job property
     * 
     * @param name of the job property
     * 
     * @return null if not found, otherwise the properties value
     */
    public String getJobProperty(String propertyName) {

        if (jobProperties != null) {
            for (JobProperty jobProperty : jobProperties) {
                if (jobProperty != null && propertyName.equals(jobProperty.getName()) && jobProperty.getValue() != null && !jobProperty.getValue().trim().isEmpty()) {
                    return jobProperty.getValue();
                }
            }
        }

        return null;
    }
    
    
    /**
     * Returns the running state of this job
     * 
     * @return true if the job is running
     */
    public boolean isBuilding()
    {
        return getLastBuild() != null && getLastBuild().getBuilding();
    }

    /**
     * Returns the duration of the last successfull build
     * 
     * @return the job duration of 0 if not available
     */
    public long getLastSuccessfulDuration() {
        
        if (getLastSuccessfulBuild() != null)
        {
            return getLastSuccessfulBuild().getDuration();
        }
        else
        {
            return 0;
        }
    }
}
