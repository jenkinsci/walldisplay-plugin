/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.pellepelster.jenkins.walldisplay.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author pelle
 */
public class MatrixProject extends BaseProject {

    private List<ActiveConfiguration> activeConfigurations = new ArrayList<ActiveConfiguration>();
    private List<BaseProject> jobs = new ArrayList<BaseProject>();

    public List<BaseProject> getJobs() {
        return jobs;
    }

    public void setJobs(List<BaseProject> jobs) {
        this.jobs = jobs;
    }

    public List<ActiveConfiguration> getActiveConfigurations() {
        return activeConfigurations;
    }

    public void setActiveConfigurations(List<ActiveConfiguration> activeConfigurations) {
        this.activeConfigurations = activeConfigurations;
    }

    private int getNumberOfBuildingJobs() {
        int result = 0;

        for (BaseProject job : jobs) {
            if (job.getLastBuild() != null) {
                if (job.getLastBuild().getBuilding()) {
                    result++;
                }
            }
        }

        return result;
    }

    public boolean isBuilding() {
        return getNumberOfBuildingJobs() > 0;
    }

    public long getTotalDuration() {

        int result = 0;

        for (BaseProject job : jobs) {
            if (job.getLastSuccessfulBuild() != null) {
                result += job.getLastSuccessfulBuild().getDuration();
            }
        }

        return result;

    }
}
