package de.pellepelster.jenkins.walldisplay;

import hudson.Plugin;
import hudson.Util;
import hudson.XmlFile;
import hudson.model.Api;
import hudson.model.Hudson;
import hudson.model.TransientViewActionFactory;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.export.Exported;
import org.kohsuke.stapler.export.ExportedBean;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;

/**
 * This plugin provides a application that monitors jobs in a way suitable for public wall displays
 *
 * @author Christian Pelster
 * @plugin jenkinswalldisplay
 */
@ExportedBean
public class WallDisplayPlugin extends Plugin {

    private static final Logger LOGGER = Logger.getLogger("hudson." + WallDisplayPlugin.class.getName());

    public final static String PLUGIN_NAME = "jenkinswalldisplay";

    public static final String[] themes = new String[]{"Default", "Plain", "Christmas", "Boss", "Dark", "Colorblind"};

    public static final String[] fontFamilies = new String[]{"Sans-Serif", "Arial", "Helvetica", "Verdana"};

    public static final String[] buildRange = new String[]{"All", "Active this month", "Active this week", "Active today"};

    public static final String[] sortOrder = new String[]{"Job Name",
            "Job Status"};

    @Exported
    public Configuration config;

    public WallDisplayPlugin() {
    }

    public Api getApi() {
        return new Api(this);
    }

    @Exported
    public Date getDate() {
        return new Date();
    }
    
    @Exported
    public String getVersion() {
        return Hudson.getInstance().getPluginManager().getPlugin(WallDisplayPlugin.class).getVersion();
    }

    protected Configuration loadConfiguration() throws IOException {
        XmlFile xmlFile = getConfigXml();
        Configuration config = null;
        if (xmlFile.exists()) {
            config = (Configuration) xmlFile.read();
            LOGGER.info(String.format("Loaded configuration data: %s", config.toString()));
        } else {
            LOGGER.info("Could not find configuration file, creating empty object");
            config = new Configuration();
        }
        return config;
    }

    @Override
    public void start() throws Exception {
        config = loadConfiguration();
        Hudson.getInstance().getExtensionList(TransientViewActionFactory.class).add(0, new WallDisplayTransientViewActionFactory());
        super.start();
    }

    @Override
    public void configure(StaplerRequest req, JSONObject formData)
            throws IOException {

        config.setTheme(Util.fixEmptyAndTrim(formData.optString("theme")));
        config.setFontFamily(Util.fixEmptyAndTrim(formData
                .optString("fontFamily")));
        config.setBuildRange(Util.fixEmptyAndTrim(formData
                .optString("buildRange")));
        config.setJenkinsTimeOut(formData.optInt("jenkinsTimeOut"));
        config.setPaintInterval(formData.optInt("paintInterval"));
        config.setJenkinsUpdateInterval(formData
                .optInt("jenkinsUpdateInterval"));
        config.setShowLastStableTimeAgo(formData
                .optBoolean("jenkinsLastStableTimeAgo"));
        config.setBlinkBgPicturesWhenBuilding(formData
                .optBoolean("blinkBgPicturesWhenBuilding"));
        config.setShowBuildNumber(formData.optBoolean("jenkinsShowBuildNumber"));
        config.setShowDetails(formData.optBoolean("jenkinsShowDetails"));
        config.setShowGravatar(formData.optBoolean("jenkinsShowGravatar"));
        config.setShowDisabledBuilds(formData
                .optBoolean("jenkinsShowDisabledBuilds"));
        config.setSortOrder(Util.fixEmptyAndTrim(formData
                .optString("sortOrder")));
        config.setCustomTheme(formData.optString("customTheme"));
        config.setShowWeatherReport(formData.optBoolean("jenkinsShowWeatherReport"));

        getConfigXml().write(config);
    }

    @Override
    protected XmlFile getConfigXml() {
        return new XmlFile(Hudson.XSTREAM,
                new File(Hudson.getInstance().getRootDir(),
                        WallDisplayPlugin.class.getName() + ".xml"));
    }
}
