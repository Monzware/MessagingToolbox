package com.monzware.messaging.job;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import com.monzware.messaging.toolbox.MessagingToolboxPlugin;
import com.monzware.messaging.toolbox.core.configmodel.Endpoint;
import com.monzware.messaging.toolbox.core.configmodel.EndpointSender;
import com.monzware.messaging.toolbox.core.configmodel.EndpointSenderException;
import com.monzware.messaging.toolbox.util.InputStreamToStringConverter;

public class SendMessageFromIFileJob extends Job implements EndpointJob {

	private final Endpoint endpoint;
	private final IFile file;

	public SendMessageFromIFileJob(Endpoint endpoint, IFile file) {
		super("Sending content from " + file.getName() + " to " + endpoint.getName());
		this.endpoint = endpoint;
		this.file = file;

	}

	protected IStatus run(IProgressMonitor monitor) {

		EndpointSender sender = endpoint.getEndpointSender();
		if (sender != null) {

			try {

				monitor.beginTask("Read file", 2);

				InputStream contents = file.getContents();
				InputStreamToStringConverter conv = new InputStreamToStringConverter(contents);

				monitor.worked(1);
				monitor.beginTask("Send message to endpoint", 2);

				sender.sendMessage(conv.getString());

			} catch (EndpointSenderException e) {
				return new SendMessageJobErrorStatus(e);
			} catch (FileNotFoundException e) {
				return new SendMessageJobErrorStatus(e);
			} catch (IOException e) {
				return new SendMessageJobErrorStatus(e);
			} catch (CoreException e) {
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
