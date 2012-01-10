/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.pellepelster.jenkins.walldisplay;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.kohsuke.stapler.export.Exported;
import org.kohsuke.stapler.export.ExportedBean;

/**
 * Configuration holder for the walldisplay plugin
 * 
 * @author Christian Pelster
 */
@ExportedBean
public class Configuration {

    private String theme;

    private int jenkinsTimeOut = 15;

    private int jenkinsUpdateInterval = 20;

    @Exported
    public int getJenkinsTimeOut() {
        return jenkinsTimeOut;
    }

    public void setJenkinsTimeOut(int jenkinsTimeOut) {
        if (jenkinsTimeOut > 0)
        {
            this.jenkinsTimeOut = jenkinsTimeOut;
        }
    }

        

    @Exported
    public int getJenkinsUpdateInterval() {
        return jenkinsUpdateInterval;
    }

    public void setJenkinsUpdateInterval(int jenkinsUpdateInterval) {
        if (jenkinsUpdateInterval > 0)
        {
            this.jenkinsUpdateInterval = jenkinsUpdateInterval;
        }
    }
    
    @Exported
    public String getTheme() {
        return theme;
    }

    public boolean isValid() {
        return (theme != null);
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("theme", theme).append("jenkinsTimeOut", jenkinsTimeOut).append("jenkinsUpdateInterval", jenkinsUpdateInterval).toString();
    }
}