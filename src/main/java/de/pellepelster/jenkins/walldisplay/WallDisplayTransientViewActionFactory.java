package de.pellepelster.jenkins.walldisplay;

import hudson.model.*;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
                owner = ((hudson.plugins.nested_view.NestedView) owner).getOwner();
            }
            Collections.reverse(viewNames);
            viewName = StringUtils.join(viewNames, "/view/");
        }

        result.add(new WallDisplayViewAction(viewName));
        return result;

    }

}
