package de.pellepelster.jenkins.walldisplay;

import java.io.IOException;
import java.net.URL;

import javax.swing.SwingWorker;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.mapper.MapperWrapper;
import com.trilead.ssh2.crypto.Base64;
import de.pellepelster.jenkins.walldisplay.model.ActiveConfiguration;
import de.pellepelster.jenkins.walldisplay.model.BaseProject;

import de.pellepelster.jenkins.walldisplay.model.Build;
import de.pellepelster.jenkins.walldisplay.model.Hudson;
import de.pellepelster.jenkins.walldisplay.model.Item;
import de.pellepelster.jenkins.walldisplay.model.FreeStyleProject;
import de.pellepelster.jenkins.walldisplay.model.JobProperty;
import de.pellepelster.jenkins.walldisplay.model.MatrixConfiguration;
import de.pellepelster.jenkins.walldisplay.model.MatrixProject;
import de.pellepelster.jenkins.walldisplay.model.PropertyConverter;
import de.pellepelster.jenkins.walldisplay.model.Queue;
import de.pellepelster.jenkins.walldisplay.model.Task;
import de.pellepelster.jenkins.walldisplay.model.View;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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

        private String jobName;
        private List<BaseProject> jobs;
        private String jobUrl;

        public JobApiRunnable(List<BaseProject> jobs, String jobName, String jobUrl) {
            this.jobName = jobName;
            this.jobs = jobs;
            this.jobUrl = jobUrl;
        }

        @Override
        public void run() {

            try {
                URL jobApiUrl = new URL(String.format("%s/api/xml?depth=1", jobUrl));

                XStream jobXStream = getDefaultXStream();
                jobXStream.alias("matrixProject", MatrixProject.class);
                jobXStream.alias("job", FreeStyleProject.class);
                jobXStream.alias("lastSuccessfulBuild", Build.class);
                jobXStream.alias("build", Build.class);
                jobXStream.alias("matrixConfiguration", MatrixConfiguration.class);
                jobXStream.alias("freeStyleProject", FreeStyleProject.class);
                jobXStream.addImplicitCollection(FreeStyleProject.class, "jobProperties", "property", JobProperty.class);
                jobXStream.addImplicitCollection(MatrixProject.class, "activeConfigurations", "activeConfiguration", ActiveConfiguration.class);
                jobXStream.registerConverter(new PropertyConverter());

                BaseProject detailedJob = (BaseProject) jobXStream.fromXML(openStream(jobApiUrl));

                jobs.add(detailedJob);

                if (detailedJob instanceof MatrixProject) {
                    MatrixProject matrixProject = (MatrixProject) detailedJob;
                    matrixProject.setJobs(new ArrayList<BaseProject>());

                    ExecutorService executor = Executors.newFixedThreadPool(JOB_API_EXECUTORS);

                    //load detailed jobs infos for all displayed jobs
                    for (ActiveConfiguration activeConfiguration : matrixProject.getActiveConfigurations()) {
                        
                        if (activeConfiguration != null)
                        {
                            executor.execute(new JobApiRunnable(Collections.synchronizedList(matrixProject.getJobs()), activeConfiguration.getName(), String.format("%s/./%s", jobUrl, activeConfiguration.getName())));
                        }
                    }

                    executor.shutdown();
                    executor.awaitTermination(JOB_API_TIMEOUT, TimeUnit.MILLISECONDS);
                    matrixProject.toString();

                }

            } catch (Exception e) {
                BaseProject job = new BaseProject();
                job.setName(jobName);
                job.setColor("grey");
                jobs.add(job);
                System.out.println(e.getMessage());
            }

        }
    }

    private long getServerResponseTime(String jenkinsUrl) {

        try {
            URL url = new URL(jenkinsUrl);
            URLConnection conn = url.openConnection();

            return conn.getHeaderFieldDate("Date", 0);

        } catch (Exception e) {
            return 0;
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
            hudsonXstream.alias("job", BaseProject.class);
            hudsonXstream.alias("view", View.class);
            hudsonXstream.addImplicitCollection(Hudson.class, "views", "view", View.class);
            hudsonXstream.addImplicitCollection(Hudson.class, "jobs", "job", BaseProject.class);
            hudsonXstream.addImplicitCollection(View.class, "jobs", "job", BaseProject.class);

            Hudson hudson = (Hudson) hudsonXstream.fromXML(openStream(hudsonApiUrl));
            hudson.setServerResponseTimestamp(getServerResponseTime(jenkinsUrl));

            List<BaseProject> jobsToLoad = new ArrayList<BaseProject>(hudson.getJobs());
            for (View view : hudson.getViews()) {
                if (viewName != null && viewName.equals(view.getName())) {
                    jobsToLoad = view.getJobs();
                }
            }

            ExecutorService executor = Executors.newFixedThreadPool(JOB_API_EXECUTORS);

            List<BaseProject> jobDetails = new ArrayList<BaseProject>();
            
            //load detailed jobs infos for all displayed jobs
            for (BaseProject job : jobsToLoad) {
                executor.execute(new JobApiRunnable(Collections.synchronizedList(jobDetails), job.getName(), String.format("%s/job/%s", jenkinsUrl, job.getName().replace(" ", "%20"))));
            }
            hudson.setJobs(jobDetails);

            executor.shutdown();
            executor.awaitTermination(JOB_API_TIMEOUT, TimeUnit.MILLISECONDS);

            if (queue.getItems() != null) {
                int i = queue.getItems().size() + 1;
                for (Item item : queue.getItems()) {
                    i--;
                    for (BaseProject job : jobDetails) {
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

    public static List<BaseProject> getJobsToDisplay(Hudson hudson, String viewName) {

        List<BaseProject> result = hudson.getJobs();

        for (View view : hudson.getViews()) {
            if (view.getName().equals(viewName)) {
                result = view.getJobs();
            }
        }

        return result;
    }
}
