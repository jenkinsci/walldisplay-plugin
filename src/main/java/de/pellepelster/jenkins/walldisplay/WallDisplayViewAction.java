package de.pellepelster.jenkins.walldisplay;

import hudson.model.Action;
import hudson.model.Hudson;
import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;

/**
 * Action providing the link to the actual walldisplay page
 * 
 * @author Christian Pelster
 */
public class WallDisplayViewAction implements Action {

    private String viewName;

    public WallDisplayViewAction(String viewName) {
        this.viewName = viewName;
    }

    @Override
    public String getIconFileName() {
        return "/plugin/jenkinswalldisplay/images/icon.png";
    }

    @Override
    public String getDisplayName() {
        return "Wall Display";
    }

    @Override
    public String getUrlName() {

        String encodedUrl = null;
        try {
            encodedUrl = URLEncoder.encode(Hudson.getInstance().getRootUrl(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            encodedUrl = Hudson.getInstance().getRootUrl();
        }

        return String.format("%s/plugin/jenkinswalldisplay/walldisplay.html?viewName=%s&jenkinsUrl=%s", Hudson.getInstance().getRootUrl(), viewName, encodedUrl);
    }
}
