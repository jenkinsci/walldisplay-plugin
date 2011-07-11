package de.pellepelster.jenkins.walldisplay;

import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.model.JobProperty;
import hudson.model.JobPropertyDescriptor;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.export.Exported;
import org.kohsuke.stapler.export.ExportedBean;

/**
 *
 * @author pelle
 */
@ExportedBean(defaultVisibility=0)
public class WallDisplayJobProperty extends JobProperty<AbstractProject<?, ?>> {

    @Exported
    public String wallDisplayName = null;

    @DataBoundConstructor
    public WallDisplayJobProperty(String wallDisplayName) {
        this.wallDisplayName = wallDisplayName;
    }

    public String getWallDisplayName() {
        return wallDisplayName;
    }

    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl) super.getDescriptor();
    }

    @Extension
    public static final class DescriptorImpl extends JobPropertyDescriptor {

        private String wallDisplayName;

        public DescriptorImpl() {
            super(WallDisplayJobProperty.class);
            load();
        }

        @Override
        public String getDisplayName() {
            return "Buildnumber Group";
        }

        @Override
        public boolean configure(StaplerRequest req, JSONObject json)
                throws hudson.model.Descriptor.FormException {
            try {
                wallDisplayName = json.getString("wallDisplayName");
            } catch (Exception e) {
                wallDisplayName = null;
            }

            save();

            return super.configure(req, json);
        }

        public String getWallDisplayName() {
            return wallDisplayName;
        }
    }
}