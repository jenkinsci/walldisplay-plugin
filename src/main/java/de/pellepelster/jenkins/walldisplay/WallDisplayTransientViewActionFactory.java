package de.pellepelster.jenkins.walldisplay;

import hudson.model.Action;
import hudson.model.TransientViewActionFactory;
import hudson.model.ViewGroup;
import hudson.model.Hudson;
import hudson.model.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * Adds the walldisplay link action to all views
 * 
 * @author Christian Pelster
 */
public class WallDisplayTransientViewActionFactory extends TransientViewActionFactory {

    @Override
    public List<Action> createFor(final View view) {

        List<Action> result = new ArrayList<Action>();
        String viewName = view.getViewName();

        if (Hudson.getInstance().getPlugin("nested-view") != null) {
            ArrayList<String> viewNames = new ArrayList<String>();
            viewNames.add(viewName);
            ViewGroup owner = view.getOwner();
            while (owner instanceof hudson.plugins.nested_view.NestedView) {
                viewNames.add(((hudson.plugins.nested_view.NestedView) owner).getViewName());
            }
            Collections.reverse(viewNames);
            viewName = StringUtils.join(viewNames, "/view/");
        }

        result.add(new WallDisplayViewAction(viewName));
        return result;

    }

}
