package de.pellepelster.jenkins.walldisplay.model;

/**
 * Xstream object for build information
 * 
 * @author pelle
 */
public class Build {

    private Integer duration = 0;
    private Integer number = 0;
    private Boolean building = false;
    private Long timestamp = 0l;
    private String color;

    /**
     * @return the duration
     */
    public Integer getDuration() {
        return duration;
    }

    /**
     * @param duration to set
     */
    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    /**
     * @return the number
     */
    public Integer getNumber() {
        return number;
    }

    /**
     * @param number to set
     */
    public void setNumber(Integer number) {
        this.number = number;
    }

    /**
     * @return the building state
     */
    public Boolean getBuilding() {
        return building;
    }

    /**
     * @param building state to set
     */
    public void setBuilding(Boolean building) {
        this.building = building;
    }

    /**
     * @return the timestamp
     */
    public Long getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp to set
     */
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @return the color
     */
    public String getColor() {
        return color;
    }

    /**
     * @param color to set
     */
    public void setColor(String color) {
        this.color = color;
    }

}
