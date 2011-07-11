package de.pellepelster.jenkins.walldisplay.model;

/**
 * 
 * @author pelle
 */
public class Job {

	private String displayName;
	private String name;
	private String url;
	private String color;
	private Build lastSuccessfulBuild;
	private Build lastBuild;
	private Integer queuePosition;
	private String description;
        private JobProperty property;

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
	 * @return the displayName configured for this job
	 */
	public JobProperty getProperty() {
		return property;
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
	 * @param walldisplayName to set
	 */
	public void setProperty(JobProperty property) {
		this.property = property;
	}
        
	/**
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @param build the build to set
	 */
	public void setLastBuild(Build lastBuild) {
		this.lastBuild = lastBuild;
	}

	/**
	 * @param lastSuccessfulBuild the lastSuccessfulBuild to set
	 */
	public void setLastSuccessfulBuild(Build lastSuccessfulBuild) {
		this.lastSuccessfulBuild = lastSuccessfulBuild;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param queuePosition the queuePosition to set
	 */
	public void setQueuePosition(Integer queuePosition) {
		this.queuePosition = queuePosition;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

}
