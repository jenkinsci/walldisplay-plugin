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
	private String viewOwnerUrl;

	public WallDisplayViewAction(String viewName, String viewOwnerUrl) {
                try {
                        this.viewName = URLEncoder.encode(viewName, ENCODING);
                } catch (UnsupportedEncodingException e) {
                        // TODO
                }
		this.viewOwnerUrl = viewOwnerUrl;
	}

	public String getIconFileName() {
		return "/plugin/jenkinswalldisplay/images/icon.png";
	}

	public String getDisplayName() {
		return "Wall Display";
	}

	public String getUrlName() {
		String hudsonUrl = getRootUrl();
		try {
			hudsonUrl = URLEncoder.encode(hudsonUrl, ENCODING);
		} catch (UnsupportedEncodingException e) {
			// TODO
		}
		return String
				.format("%splugin/jenkinswalldisplay/walldisplay.html?viewName=%s&jenkinsUrl=%s%s",
						getRootUrl(), viewName, hudsonUrl, viewOwnerUrl);
	}
	
	public String getRootUrl(){
		return Hudson.getInstance().getRootUrl();
	}

}
