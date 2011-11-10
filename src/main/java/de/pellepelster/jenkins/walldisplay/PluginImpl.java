package de.pellepelster.jenkins.walldisplay;

import java.io.IOException;
import java.io.Writer;

import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import hudson.Plugin;
import hudson.model.Hudson;
import hudson.model.TransientViewActionFactory;
import org.apache.commons.lang.StringUtils;

/**
 * This plugin provides a application that monitors jobs in a way suitable for public wall displays
 *
 * @author Christian Pelster
 * @plugin jenkinswalldisplay
 */
public class PluginImpl extends Plugin {

    public final static String PLUGIN_NAME = "jenkinswalldisplay";

    @Override
    public void start() throws Exception {
        super.start();

        Hudson.getInstance().getExtensionList(TransientViewActionFactory.class).add(0, new WallDisplayTransientViewActionFactory());
    }
}
