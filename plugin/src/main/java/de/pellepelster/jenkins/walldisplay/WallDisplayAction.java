package de.pellepelster.jenkins.walldisplay;

import hudson.Extension;
import hudson.model.RootAction;

@Extension
public class WallDisplayAction implements RootAction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getDisplayName() {
		return "Jenkins Wall Display";
	}

	public String getIconFileName() {
		return "/plugin/jenkinswalldisplay/images/icon.png";
	}

	public String getUrlName() {
		return "plugin/jenkinswalldisplay/launch";
	}
        
}
