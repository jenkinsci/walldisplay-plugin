package de.pellepelster.jenkins.walldisplay;

import hudson.model.Action;
import hudson.model.TransientViewActionFactory;
import hudson.model.View;
import java.util.ArrayList;
import java.util.List;

/**
 * Adds the walldisplay link action to all views
 * 
 * @author Christian Pelster
 */
public class WallDisplayTransientViewActionFactory extends TransientViewActionFactory {

    @Override
    public List<Action> createFor(View view) {

        List<Action> result = new ArrayList<Action>();
        result.add(new WallDisplayViewAction(view.getViewName()));
        return result;

    }

}
