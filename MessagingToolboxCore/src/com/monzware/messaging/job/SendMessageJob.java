package com.monzware.messaging.job;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import com.monzware.messaging.toolbox.MessagingToolboxPlugin;
import com.monzware.messaging.toolbox.core.configmodel.Endpoint;
import com.monzware.messaging.toolbox.core.configmodel.EndpointSender;
import com.monzware.messaging.toolbox.core.configmodel.EndpointSenderException;

public class SendMessageJob extends Job implements EndpointJob {

	private final Endpoint endpoint;
	private final String dataString;

	public SendMessageJob(Endpoint endpoint, String dataString) {
		super("Sending text to " + endpoint.getName());
		this.endpoint = endpoint;
		this.dataString = dataString;
	}

	protected IStatus run(IProgressMonitor monitor) {

		EndpointSender sender = endpoint.getEndpointSender();
		if (sender != null) {
			try {
				sender.sendMessage(dataString);
				monitor.done();
			} catch (EndpointSenderException e) {
				return new SendMessageJobErrorStatus(e);
			}
		}

		return Status.OK_STATUS;

	}

	public String getEndpointName() {
		return endpoint.getName();
	}

	public boolean belongsTo(Object family) {
		return (MessagingToolboxPlugin.PLUGIN_ID + "#JOB").equals(family);
	}

}
