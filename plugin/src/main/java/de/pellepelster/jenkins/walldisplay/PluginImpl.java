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

    public void doLaunch(StaplerRequest req, StaplerResponse res) throws IOException {

        String viewName = req.getOriginalRestOfPath();
        viewName = StringUtils.removeStart(viewName, "/");

        res.setHeader("Content-Disposition", "filename=JenkinsWallDisplay.jnlp");
        res.setContentType("application/x-java-jnlp-file");
        Writer w = res.getWriter();

        w.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        w.write("<jnlp spec=\"1.0+\" codebase=\"" + req.getRootPath() + "/plugin/" + PLUGIN_NAME + "\">");
        w.write("<information>");
        w.write("<title>Jenkins Wall Display</title>");
        w.write("<vendor>Christian Pelster</vendor>");
        w.write("<description>Jenkins Wall Display Application</description>");
        w.write("<description kind=\"short\"></description>");
        w.write("<offline-allowed/>");
        w.write("</information>");
        w.write("<security>");
        w.write("<all-permissions/>");
        w.write("</security>");
        w.write("<resources>");
        w.write("<j2se version=\"1.6\"/>");
        w.write("<jar href=\"client.jar\"/>");
        w.write("<jar href=\"commons-cli.jar\"/>");
        w.write("<jar href=\"xpp3.jar\"/>");
        w.write("<jar href=\"xstream.jar\"/>");
        w.write("</resources>");
        w.write("<application-desc main-class=\"de.pellepelster.jenkins.walldisplay.WallDisplayFrame\">");
        w.write("<argument>" + req.getRootPath() + "</argument>");
        w.write("<argument>" + viewName + "</argument>");
        w.write("</application-desc>");
        w.write("</jnlp>");

    }
}
