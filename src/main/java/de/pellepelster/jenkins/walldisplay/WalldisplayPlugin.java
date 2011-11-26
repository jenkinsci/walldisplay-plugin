package de.pellepelster.jenkins.walldisplay;

import java.io.IOException;
import java.io.Writer;

import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import hudson.Plugin;
import hudson.model.Api;
import hudson.model.Hudson;
import hudson.model.TransientViewActionFactory;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.export.Exported;
import org.kohsuke.stapler.export.ExportedBean;

/**
 * This plugin provides a application that monitors jobs in a way suitable for public wall displays
 *
 * @author Christian Pelster
 * @plugin jenkinswalldisplay
 */
@ExportedBean
public class WalldisplayPlugin extends Plugin {

    public final static String PLUGIN_NAME = "jenkinswalldisplay";

    private final String theme;
    
    @DataBoundConstructor
    public WalldisplayPlugin(String theme) {
        this.theme = theme;
    }
    
    public Api getApi() {
        return new Api(this);
    }

    @Exported
    public String getVersion() {
        return Hudson.getInstance().getPluginManager().getPlugin(WalldisplayPlugin.class).getVersion();
    }

    @Override
    public void start() throws Exception {
        super.start();

        Hudson.getInstance().getExtensionList(TransientViewActionFactory.class).add(0, new WallDisplayTransientViewActionFactory());
    }
}
