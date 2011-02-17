package de.pellepelster.jenkins.walldisplay;

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

/**
 * 
 * @author pelle
 */
public class HudsonWorker extends SwingWorker<Hudson, Void> {

	private String hudsonUrl;
	private Exception exception = null;

	public HudsonWorker(String hudsonUrl) {
		this.hudsonUrl = hudsonUrl;
	}

	/** {@inheritDoc} */
	@Override
	protected Hudson doInBackground() throws Exception {
		exception = null;
		return read();
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

	public Hudson read() {

		try {
			URL hudsonQueueApiUrl = new URL(String.format("%s/queue/api/xml", hudsonUrl));

			XStream queueXstream = getDefaultXStream();
			queueXstream.alias("queue", Queue.class);
			queueXstream.alias("task", Task.class);
			queueXstream.addImplicitCollection(Queue.class, "items", "item", Item.class);

			Queue queue = (Queue) queueXstream.fromXML(hudsonQueueApiUrl.openStream());

			URL hudsonApiUrl = new URL(String.format("%s/api/xml", hudsonUrl));

			XStream hudsonXstream = getDefaultXStream();
			hudsonXstream.alias("hudson", Hudson.class);
			hudsonXstream.alias("job", Job.class);
			hudsonXstream.addImplicitCollection(Hudson.class, "jobs", "job", Job.class);

			Hudson hudson = (Hudson) hudsonXstream.fromXML(hudsonApiUrl.openStream());

			for (Job job : hudson.getJobs()) {
				URL jobApiUrl = new URL(String.format("%s/job/%s/api/xml?depth=1", hudsonUrl, job.getName()));

				XStream jobXStream = getDefaultXStream();
				jobXStream.alias("job", Job.class);
				jobXStream.alias("freeStyleProject", Job.class);
				jobXStream.alias("lastSuccessfulBuild", Build.class);
				jobXStream.alias("build", Build.class);

				jobXStream.fromXML(jobApiUrl.openStream(), job);
			}

			if (queue.getItems() != null) {
				int i = queue.getItems().size() + 1;
				for (Item item : queue.getItems()) {
					i--;
					for (Job job : hudson.getJobs()) {
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

}
