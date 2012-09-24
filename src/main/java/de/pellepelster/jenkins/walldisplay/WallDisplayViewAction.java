package de.pellepelster.jenkins.walldisplay;

import hudson.model.Action;
import hudson.model.Hudson;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Action providing the link to the actual walldisplay page
 * 
 * @author Christian Pelster
 */
public class WallDisplayViewAction implements Action {

    private static final String ENCODING = "UTF-8";
    private String viewName;

    public WallDisplayViewAction(String viewName) {
        this.viewName = viewName;
    }

    public String getIconFileName() {
        return "/plugin/jenkinswalldisplay/images/icon.png";
    }

    public String getDisplayName() {
        return "Wall Display";
    }

    public String getUrlName() {

        String encodedUrl = null;
        String encodedViewName = null;
        String rootUrl = getRootUrl();
        try {
            encodedUrl = URLEncoder.encode(rootUrl, ENCODING);
            encodedViewName = URLEncoder.encode(viewName, ENCODING);
        } catch (UnsupportedEncodingException e) {
            encodedUrl = rootUrl;
            encodedViewName = viewName;
        }
        return String
                .format("%s/plugin/jenkinswalldisplay/walldisplay.html?viewName=%s&jenkinsUrl=%s",
                        rootUrl, encodedViewName, encodedUrl);
    }

    protected String getRootUrl() {
        return Hudson.getInstance().getRootUrl();
    }
}
