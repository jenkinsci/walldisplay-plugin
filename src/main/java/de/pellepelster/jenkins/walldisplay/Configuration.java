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

    private String theme = WallDisplayPlugin.themes[0];
    private String fontFamily = WallDisplayPlugin.fontFamilies[0];
    private String buildRange = WallDisplayPlugin.buildRange[0];
    private int jenkinsTimeOut = 15;
    private int jenkinsUpdateInterval = 20;
    private Boolean showDetails = false;
    private Boolean showGravatar = false;
    private Boolean showBuildNumber = true;
    private Boolean showDisabledBuilds = true;

    @Exported
    public Boolean getShowBuildNumber() {
        return showBuildNumber;
    }

    public void setShowBuildNumber(Boolean showBuildNumber) {
        this.showBuildNumber = showBuildNumber;
    }

    @Exported
    public Boolean getShowDisabledBuilds() {
        return showDisabledBuilds;
    }

    public void setShowDisabledBuilds(Boolean showDisabledBuilds) {
        this.showDisabledBuilds = showDisabledBuilds;
    }

    @Exported
    public int getJenkinsTimeOut() {
        return jenkinsTimeOut;
    }

    public void setJenkinsTimeOut(int jenkinsTimeOut) {
        if (jenkinsTimeOut > 0) {
            this.jenkinsTimeOut = jenkinsTimeOut;
        }
    }

    @Exported
    public int getJenkinsUpdateInterval() {
        return jenkinsUpdateInterval;
    }

    public void setJenkinsUpdateInterval(int jenkinsUpdateInterval) {
        if (jenkinsUpdateInterval > 0) {
            this.jenkinsUpdateInterval = jenkinsUpdateInterval;
        }
    }

    @Exported
    public String getTheme() {
        if (theme != null)
        {
            return theme.toLowerCase();
        }
        else
        {
            return theme;
        }
    }

    public Boolean isValid() {
        return (theme != null && buildRange  != null);
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    @Exported
    public String getBuildRange() {
        if (buildRange != null)
        {
            return buildRange.toLowerCase();
        }
        else
        {
            return buildRange;
        }
    }

    public void setBuildRange(String buildRange) {
        this.buildRange = buildRange;
    }

    @Exported
    public boolean getShowDetails() {
        return showDetails;
    }

    public void setShowDetails(Boolean showDetails) {
        this.showDetails = showDetails;
    }

    @Exported
    public Boolean getShowGravatar() {
        return showGravatar;
    }

    public void setShowGravatar(Boolean showGravatar) {
        this.showGravatar = showGravatar;
    }

    @Exported
    public String getFontFamily() {
        if (fontFamily != null)
        {
            return fontFamily.toLowerCase();
        }
        else
        {
            return fontFamily;
        }
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("theme", theme).append("jenkinsTimeOut", jenkinsTimeOut).append("jenkinsUpdateInterval", jenkinsUpdateInterval).append("showDetails", showDetails).append("showGravatar", showGravatar).append("fontFamily", fontFamily).append("buildRange", buildRange).toString();
    }
}