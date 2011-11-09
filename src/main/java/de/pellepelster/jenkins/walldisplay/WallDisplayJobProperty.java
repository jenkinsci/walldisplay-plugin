package de.pellepelster.jenkins.walldisplay;

import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.model.Job;
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
@ExportedBean
public final class WallDisplayJobProperty extends JobProperty<AbstractProject<?, ?>> {

    private String wallDisplayName = null;

    @DataBoundConstructor
    public WallDisplayJobProperty(String wallDisplayName) {
        this.wallDisplayName = wallDisplayName;
    }

    @Exported
    public String getWallDisplayName() {
        return wallDisplayName;
    }

    @Extension
    public static final class DescriptorImpl extends JobPropertyDescriptor {

        public DescriptorImpl() {
            super(WallDisplayJobProperty.class);
            load();
        }

        @Override
        public boolean isApplicable(Class<? extends Job> jobType) {
            return AbstractProject.class.isAssignableFrom(jobType);
        }

        public String getDisplayName() {
            return "Walldisplay";
        }

        @Override
        public JobProperty<?> newInstance(StaplerRequest req,
                JSONObject formData) throws FormException {

            if (formData.has("wallDisplayNameDynamic"))
            {
                JSONObject wallDisplayNameDynamic = formData.getJSONObject("wallDisplayNameDynamic");

                if (wallDisplayNameDynamic != null && wallDisplayNameDynamic.has("wallDisplayName") && !wallDisplayNameDynamic.get("wallDisplayName").toString().trim().isEmpty()) {
                    return req.bindJSON(WallDisplayJobProperty.class, wallDisplayNameDynamic);
                }
            }
            
            return new WallDisplayJobProperty(null);
        }
    }
}
