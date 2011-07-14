package de.pellepelster.jenkins.walldisplay.model;

import java.util.ArrayList;
import java.util.List;

/**
 * XStream object for matrix projects
 * 
 * @author pelle
 */
public class MatrixProject extends BaseProject {

    private List<BaseProject> jobs = new ArrayList<BaseProject>();

    public List<BaseProject> getJobs() {
        return jobs;
    }

    public void setJobs(List<BaseProject> jobs) {
        this.jobs = jobs;
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

    @Override
    public boolean isBuilding() {
        return getNumberOfBuildingJobs() > 0;
    }

    @Override
    public long getLastSuccessfulDuration() {

        int result = 0;

        for (BaseProject job : jobs) {
            if (job.getLastSuccessfulBuild() != null) {
                result += job.getLastSuccessfulBuild().getDuration();
            }
        }

        return result;

    }
}
