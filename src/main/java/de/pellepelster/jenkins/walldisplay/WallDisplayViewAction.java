package de.pellepelster.jenkins.walldisplay;

import hudson.FilePath;
import hudson.model.Action;
import hudson.model.Hudson;
import java.io.File;
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
        
        String s7 = Hudson.RESOURCE_PATH;
        
        String s1 = Hudson.getInstance().getRootUrl();
        String s2 = Hudson.getInstance().getRootUrlFromRequest();
        FilePath f1 = Hudson.getInstance().getRootPath();
        String s5 = f1.toString().toString();
        File f2 = Hudson.getInstance().getRootDir();
        String s6 = f2.toURI().toString();

        return String.format("%s/plugin/jenkinswalldisplay/walldisplay.html?viewName=%s&jenkinsUrl=%s", Hudson.getInstance().getRootUrl(), viewName, encodedUrl);
    }
}
