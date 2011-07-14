/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.pellepelster.jenkins.walldisplay;

import hudson.model.Action;
import hudson.model.Hudson;

/**
 *
 * @author pelle
 */
public class WallDisplayViewAction implements  Action {

    private String viewName;

    public WallDisplayViewAction(String viewName)
    {
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
        return String.format("%s/plugin/jenkinswalldisplay/launch/%s", Hudson.getInstance().getRootUrl(), viewName);
    }

}
