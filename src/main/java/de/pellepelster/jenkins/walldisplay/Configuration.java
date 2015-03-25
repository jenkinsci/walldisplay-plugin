/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.pellepelster.jenkins.walldisplay;

import java.util.Date;
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
    private String sortOrder = WallDisplayPlugin.sortOrder[0];
    private String customTheme = ".custom {\r\n"
            + "        background-color: #000000;\r\n"
            + "}\r\n"
            + ".custom.blue {\r\n"
            + "        background-color: #00CC00;\r\n"
            + "}\r\n"
            + ".custom.red, .custom.yellow, .custom.aborted, .custom.grey {\r\n"
            + "        background-color: #CC0000;\r\n" + "}\r\n"
            + ".custom.claimed {\r\n"
            + "        background-color: #0000CC;\r\n" + "}\r\n" + "";
    private int jenkinsTimeOut = 15;
    private int jenkinsUpdateInterval = 20;
    private int paintInterval = 1;
    private int maxQueuePositionToShow = 15;
    private Boolean showDetails = false;
    private Boolean showGravatar = false;
    private Boolean showJunitResults = false;
    private Boolean showBuildNumber = true;
    private Boolean showLastStableTimeAgo = true;
    private Boolean showDisabledBuilds = true;
    private Boolean showWeatherReport = false;
    private Boolean blinkBgPicturesWhenBuilding = false;
	private String gravatarUrl = "http://www.gravatar.com/avatar/";

    @Exported
    public Boolean getShowBuildNumber() {
        return showBuildNumber;
    }

    @Exported
    public Boolean getShowJunitResults() {
        return showJunitResults;
    }
    
    public void setShowBuildNumber(Boolean showBuildNumber) {
        this.showBuildNumber = showBuildNumber;
    }

    public void setMaxQueuePositionToShow(int maxQueuePositionToShow) {
        this.maxQueuePositionToShow = maxQueuePositionToShow;
    }

    @Exported
    public int getMaxQueuePositionToShow() {
        return maxQueuePositionToShow;
    }    

    @Exported
    public Boolean getShowLastStableTimeAgo() {
        return showLastStableTimeAgo;
    }

    public void setShowLastStableTimeAgo(Boolean showLastStableTimeAgo) {
        this.showLastStableTimeAgo = showLastStableTimeAgo;
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
    public String getSortOrder() {
        if (sortOrder != null) {
            return sortOrder.toLowerCase();
        } else {
            return sortOrder;
        }
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    @Exported
    public String getTheme() {
        if (theme != null) {
            return theme.toLowerCase();
        } else {
            return theme;
        }
    }

    public Boolean isValid() {
        return (theme != null && buildRange != null);
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    @Exported
    public String getBuildRange() {
        if (buildRange != null) {
            return buildRange.toLowerCase();
        } else {
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

    public void setShowJunitResults(Boolean showJunitResults) {
        this.showJunitResults = showJunitResults;
    }

    @Exported
    public Boolean getShowGravatar() {
        return showGravatar;
    }

    public void setShowGravatar(Boolean showGravatar) {
        this.showGravatar = showGravatar;
    }

    @Exported
    public int getPaintInterval() {
        return paintInterval;
    }

    public void setPaintInterval(int paintInterval) {
        this.paintInterval = paintInterval;
    }


    @Exported
    public String getFontFamily() {
        if (fontFamily != null) {
            return fontFamily.toLowerCase();
        } else {
            return fontFamily;
        }
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    @Exported
    public Boolean getBlinkBgPicturesWhenBuilding() {
        return blinkBgPicturesWhenBuilding;
    }

    public void setBlinkBgPicturesWhenBuilding(Boolean blinkBgPicturesWhenBuilding) {
        this.blinkBgPicturesWhenBuilding = blinkBgPicturesWhenBuilding;
    }

    @Exported
    public String getCustomTheme() {
        return customTheme;
    }

    public void setCustomTheme(String customTheme) {
        this.customTheme = customTheme;
    }

	@Exported
	public String getGravatarUrl() {
		return gravatarUrl;
	}

	public void setGravatarUrl(String gravatarUrl) {
		this.gravatarUrl = gravatarUrl;
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("theme", theme).append("jenkinsTimeOut", jenkinsTimeOut).append("jenkinsUpdateInterval", jenkinsUpdateInterval).append("showDetails", showDetails).append("showGravatar", showGravatar).append("fontFamily", fontFamily).append("buildRange", buildRange).append("sortOrder", sortOrder).append("showWeatherReport", showWeatherReport).append("showJunitResults", showJunitResults).append("maxQueuePositionToShow", maxQueuePositionToShow).append("gravatarUrl", gravatarUrl).toString();
    }

    @Exported
    public Boolean getShowWeatherReport() {
        return showWeatherReport;
    }

    public void setShowWeatherReport(Boolean showWeatherReport) {
        this.showWeatherReport = showWeatherReport;
    }
}