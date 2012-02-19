package com.monzware.messaging.job;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import com.monzware.messaging.toolbox.MessagingToolboxPlugin;
import com.monzware.messaging.toolbox.core.configmodel.Endpoint;
import com.monzware.messaging.toolbox.core.configmodel.EndpointSender;
import com.monzware.messaging.toolbox.core.configmodel.EndpointSenderException;
import com.monzware.messaging.toolbox.util.InputStreamToStringConverter;

public class SendMessageFromFileJob extends Job implements EndpointJob {

	private final Endpoint endpoint;

	private final File file;

	public SendMessageFromFileJob(Endpoint endpoint, File file) {
		super("Sending content from " + file.getAbsolutePath() + " to " + endpoint.getName());
		this.endpoint = endpoint;
		this.file = file;
	}

	protected IStatus run(IProgressMonitor monitor) {

		EndpointSender sender = endpoint.getEndpointSender();
		if (sender != null) {

			try {

				monitor.beginTask("Read file", 2);
				FileInputStream is = new FileInputStream(file);
				InputStreamToStringConverter conv = new InputStreamToStringConverter(is);

				monitor.worked(1);
				monitor.beginTask("Send message to endpoint", 2);

				sender.sendMessage(conv.getString());

			} catch (EndpointSenderException e) {
				return new SendMessageJobErrorStatus(e);
			} catch (FileNotFoundException e) {
				return new SendMessageJobErrorStatus(e);
			} catch (IOException e) {
				return new SendMessageJobErrorStatus(e);
			}
		}

		monitor.done();
		return Status.OK_STATUS;

	}

	public String getEndpointName() {
		return endpoint.getName();
	}

	public boolean belongsTo(Object family) {
		return (MessagingToolboxPlugin.PLUGIN_ID + "#JOB").equals(family);
	}
}
