package de.pellepelster.jenkins.walldisplay;

import hudson.model.Action;
import hudson.model.TransientViewActionFactory;
import hudson.model.View;

import java.util.Arrays;
import java.util.List;

/**
 * Adds the walldisplay link action to all views
 *
 * @author Christian Pelster
 */
public class WallDisplayTransientViewActionFactory extends TransientViewActionFactory {

    @Override
    public List<Action> createFor(final View view) {
        WallDisplayViewAction action = new WallDisplayViewAction(view.getViewName(), view.getOwner().getUrl());
        return Arrays.asList((Action) action);
    }

}
