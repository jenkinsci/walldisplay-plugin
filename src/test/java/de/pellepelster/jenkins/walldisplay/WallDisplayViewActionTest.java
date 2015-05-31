package de.pellepelster.jenkins.walldisplay;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import java.io.IOException;
import java.net.URLEncoder;

import org.junit.Test;
import org.jvnet.hudson.test.HudsonTestCase;

/**
 * Unit test for WallDisplayViewAction which verifies that the wall-display
 * URL's are encoded correctly.
 * 
 * @author Joakim Sandstroem
 */
public class WallDisplayViewActionTest extends HudsonTestCase {

    private String rootUrl;
    private String encodedRootUrl;
    private String viewOwnerUrl;

    /**
     * Instantiates a new WallDisplayViewAction with a stubbed root URL
     * 
     * @param viewName
     *            view name during test
     * @return a new WallDisplayViewAction instance
     * @throws IOException
     */
    public WallDisplayViewAction newAction(String viewName, String viewOwnerUrl) throws IOException {
        WallDisplayViewAction action = spy(new WallDisplayViewAction(viewName, viewOwnerUrl));
        this.rootUrl = getURL().toExternalForm();
        this.encodedRootUrl = URLEncoder.encode(rootUrl, "UTF-8");
        this.viewOwnerUrl = viewOwnerUrl;
        doReturn(rootUrl).when(action).getRootUrl();
        return action;
    }

    /**
     * Verifies that the wall-display link for the 'All' view name is correct.
     * 
     * @throws IOException
     */
    @Test
    public void testGetUrlNameAllView() throws IOException {
        WallDisplayViewAction action = newAction("All", "/job/abc");
        String urlName = rootUrl
                + "plugin/jenkinswalldisplay/walldisplay.html?viewName=All&jenkinsUrl="
                + encodedRootUrl + viewOwnerUrl;
        assertEquals(urlName, action.getUrlName());
    }

    /**
     * Verifies that the wall-display link for a view with special characters is
     * encoded correctly.
     * 
     * @throws IOException
     */
    @Test
    public void testGetUrlNameEncodedView() throws IOException {
        WallDisplayViewAction action = newAction("+Dashboard", "");
        String urlName = rootUrl
                + "plugin/jenkinswalldisplay/walldisplay.html?viewName=%2BDashboard&jenkinsUrl="
                + encodedRootUrl + viewOwnerUrl;
        assertEquals(urlName, action.getUrlName());
    }

}
