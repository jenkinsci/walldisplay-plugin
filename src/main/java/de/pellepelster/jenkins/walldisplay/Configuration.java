/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.pellepelster.jenkins.walldisplay;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.kohsuke.stapler.export.Exported;
import org.kohsuke.stapler.export.ExportedBean;

@ExportedBean
public class Configuration {

    private String theme;

    @Exported
    public String getTheme() {
        return theme;
    }

    public boolean isValid() {
        return (theme != null);
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("theme", theme).toString();
    }
}