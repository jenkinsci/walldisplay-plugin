package de.pellepelster.jenkins.walldisplay;

import java.io.IOException;
import java.net.URL;

import javax.swing.SwingWorker;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.mapper.MapperWrapper;

import de.pellepelster.jenkins.walldisplay.model.Build;
import de.pellepelster.jenkins.walldisplay.model.Hudson;
import de.pellepelster.jenkins.walldisplay.model.Item;
import de.pellepelster.jenkins.walldisplay.model.Job;
import de.pellepelster.jenkins.walldisplay.model.Queue;
import de.pellepelster.jenkins.walldisplay.model.Task;
import de.pellepelster.jenkins.walldisplay.model.View;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Reads the jenkins remote api
 *
 * @author pelle
 */
public class JenkinsWorker extends SwingWorker<Hudson, Void> {

    private final static int CONNECT_TIMEOUT = 5000;
    private final static int READ_TIMEOUT = 5000;
    private final static int JOB_API_EXECUTORS = 5;
    private final static int JOB_API_TIMEOUT = CONNECT_TIMEOUT + READ_TIMEOUT + 1000;
    
    private String jenkinsUrl;
    private Exception exception = null;
    private String viewName;

    public JenkinsWorker(String jenkinsUrl, String viewName) {
        this.jenkinsUrl = jenkinsUrl;
        this.viewName = viewName;
    }

    private class JobApiRunnable implements Runnable {

        private Job job;

        public JobApiRunnable(Job job) {
            this.job = job;
        }

        @Override
        public void run() {

            try {
                URL jobApiUrl = new URL(String.format("%s/job/%s/api/xml?depth=1", jenkinsUrl, job.getName().replace(" ", "%20")));

                XStream jobXStream = getDefaultXStream();
                jobXStream.alias("job", Job.class);
                jobXStream.alias("freeStyleProject", Job.class);
                jobXStream.alias("lastSuccessfulBuild", Build.class);
                jobXStream.alias("build", Build.class);

                jobXStream.fromXML(openStream(jobApiUrl), job);
            } catch (Exception e) {
                job.setColor("grey");
            }

        }
    }

    /** {@inheritDoc} */
    @Override
    protected Hudson doInBackground() throws Exception {
        exception = null;

        try {
            URL hudsonQueueApiUrl = new URL(String.format("%s/queue/api/xml", jenkinsUrl));

            XStream queueXstream = getDefaultXStream();
            queueXstream.alias("queue", Queue.class);
            queueXstream.alias("task", Task.class);
            queueXstream.addImplicitCollection(Queue.class, "items", "item", Item.class);

            Queue queue = (Queue) queueXstream.fromXML(openStream(hudsonQueueApiUrl));

            URL hudsonApiUrl = new URL(String.format("%s/api/xml?depth=1", jenkinsUrl));

            XStream hudsonXstream = getDefaultXStream();
            hudsonXstream.alias("hudson", Hudson.class);
            hudsonXstream.alias("job", Job.class);
            hudsonXstream.alias("view", View.class);
            hudsonXstream.addImplicitCollection(Hudson.class, "views", "view", View.class);
            hudsonXstream.addImplicitCollection(Hudson.class, "jobs", "job", Job.class);
            hudsonXstream.addImplicitCollection(View.class, "jobs", "job", Job.class);

            Hudson hudson = (Hudson) hudsonXstream.fromXML(openStream(hudsonApiUrl));


            List<Job> jobs = hudson.getJobs();
            for (View view : hudson.getViews()) {
                if (viewName != null && viewName.equals(view.getName())) {
                    jobs = view.getJobs();
                }
            }
            
            ExecutorService executor = Executors.newFixedThreadPool(JOB_API_EXECUTORS);

            //load detailed jobs infos for all displayed jobs
            for (Job job : jobs) {
                executor.execute(new JobApiRunnable(job));
            }
            
            executor.shutdown();
            executor.awaitTermination(JOB_API_TIMEOUT, TimeUnit.MILLISECONDS);

            if (queue.getItems() != null) {
                int i = queue.getItems().size() + 1;
                for (Item item : queue.getItems()) {
                    i--;
                    for (Job job : jobs) {
                        if (item.getTask() != null && job.getName().equals(item.getTask().getName())) {
                            job.setQueuePosition(i);
                        }
                    }
                }
            }

            return hudson;

        } catch (Exception e) {
            exception = e;
            return null;
        }

    }

    private XStream getDefaultXStream() {

        XStream xstream = new XStream() {

            @Override
            protected MapperWrapper wrapMapper(MapperWrapper next) {
                return new MapperWrapper(next) {

                    @Override
                    public boolean shouldSerializeMember(Class definedIn, String fieldName) {
                        return definedIn != Object.class ? super.shouldSerializeMember(definedIn, fieldName) : false;
                    }
                };
            }
        };

        return xstream;
    }

    public Exception getException() {
        return exception;
    }

    private InputStream openStream(URL url) throws IOException {

        URLConnection conn = url.openConnection();

        conn.setConnectTimeout(CONNECT_TIMEOUT);
        conn.setReadTimeout(READ_TIMEOUT);

        return conn.getInputStream();
    }

    public static List<Job> getJobsToDisplay(Hudson hudson, String viewName) {

        List<Job> result = hudson.getJobs();

        for (View view : hudson.getViews()) {
            if (view.getName().equals(viewName)) {
                result = view.getJobs();
            }
        }

        return result;
    }
}
