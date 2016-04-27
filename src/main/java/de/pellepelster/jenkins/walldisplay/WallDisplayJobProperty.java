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
 * Job property allowing to override the name of the job that is displayed on the walldisplay
 *
 * @author Christian Pelster
 */
@ExportedBean
public final class WallDisplayJobProperty extends JobProperty<AbstractProject<?, ?>> {

    private String wallDisplayName = null;
    private String wallDisplayBgPicture = null;
    private String wallDisplayOrder = null;

    @DataBoundConstructor
    public WallDisplayJobProperty(String wallDisplayName, String wallDisplayBgPicture,String wallDisplayOrder) {
        this.wallDisplayName = wallDisplayName;
        this.wallDisplayBgPicture = wallDisplayBgPicture;
        this.wallDisplayOrder = wallDisplayOrder;
    }

    @Exported
    public String getWallDisplayName() {
        return wallDisplayName;
    }

    @Exported
    public String getWallDisplayBgPicture() {
        return wallDisplayBgPicture;
    }

    @Exported
    public String getWallDisplayOrder() {
        return wallDisplayOrder;
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

        public String getBgPicture() {
            return "";
        }

        public String getOrder() {
            return "";
        }

        @Override
        public JobProperty<?> newInstance(StaplerRequest req,
                                          JSONObject formData) throws FormException {

            String wallDisplayName = null;
            String wallDisplayBgPicture = null;
            String wallDisplayOrder = null;
            if (formData.has("wallDisplayNameDynamic")) {
                JSONObject wallDisplayNameDynamic = formData.getJSONObject("wallDisplayNameDynamic");

                if (wallDisplayNameDynamic != null && wallDisplayNameDynamic.has("wallDisplayName") && !wallDisplayNameDynamic.get("wallDisplayName").toString().trim().isEmpty()) {
                    wallDisplayName = wallDisplayNameDynamic.get("wallDisplayName").toString();
                }
            }

            if (formData.has("wallDisplayBgPictureDynamic")) {
                JSONObject wallDisplayBgPictureDynamic = formData.getJSONObject("wallDisplayBgPictureDynamic");

                if (wallDisplayBgPictureDynamic != null && wallDisplayBgPictureDynamic.has("wallDisplayBgPicture") && !wallDisplayBgPictureDynamic.get("wallDisplayBgPicture").toString().trim().isEmpty()) {
                    wallDisplayBgPicture = wallDisplayBgPictureDynamic.getString("wallDisplayBgPicture").toString();
                }
            }

            if (formData.has("wallDisplayOrderDynamic")) {
                JSONObject wallDisplayOrderDynamic = formData.getJSONObject("wallDisplayOrderDynamic");

                if (wallDisplayOrderDynamic != null && wallDisplayOrderDynamic.has("wallDisplayOrder") && !wallDisplayOrderDynamic.get("wallDisplayOrder").toString().trim().isEmpty()) {
                    wallDisplayOrder = wallDisplayOrderDynamic.get("wallDisplayOrder").toString();
                }
            }

            return new WallDisplayJobProperty(wallDisplayName, wallDisplayBgPicture, wallDisplayOrder);
        }
    }
}
