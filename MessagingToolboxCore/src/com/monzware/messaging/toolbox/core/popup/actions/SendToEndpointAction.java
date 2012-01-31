package com.monzware.messaging.toolbox.core.popup.actions;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.IJobChangeListener;
import org.eclipse.core.runtime.jobs.IJobManager;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import com.monzware.messaging.job.SendMessageFromIFileJob;
import com.monzware.messaging.job.SendMessageJob;
import com.monzware.messaging.job.SendMessageJobChangeListener;
import com.monzware.messaging.toolbox.core.configmodel.Endpoint;
import com.monzware.messaging.toolbox.core.configmodel.EndpointSender;

public class SendToEndpointAction extends Action {

	private final Endpoint endpoint;

	// private ImageDescriptor imageDescriptor =
	// MessagingToolboxPlugin.getImageDescriptor("/icons/database2.png");

	public SendToEndpointAction(Endpoint endpoint) {
		super(endpoint.getName());
		this.endpoint = endpoint;

		// setImageDescriptor(imageDescriptor);
	}

	@Override
	public void runWithEvent(Event event) {

		IWorkbench workbench = PlatformUI.getWorkbench();
		ISelectionService selectionService = workbench.getActiveWorkbenchWindow().getSelectionService();
		StructuredSelection selection = (StructuredSelection) selectionService.getSelection();

		IJobManager jm = Job.getJobManager();
		IProgressMonitor pg = jm.createProgressGroup();
		pg.setTaskName("Add files content to endpoint");

		@SuppressWarnings("unchecked")
		List<IFile> list = selection.toList();
		for (IFile iFile : list) {

			EndpointSender endpointSender = endpoint.getEndpointSender();
			if (endpointSender != null) {

				SendMessageFromIFileJob job = new SendMessageFromIFileJob(endpoint, iFile);
				IJobChangeListener listener = new SendMessageJobChangeListener(workbench.getActiveWorkbenchWindow().getShell());
				job.addJobChangeListener(listener);
				job.setUser(true);
				job.setPriority(SendMessageJob.SHORT);
				job.schedule();
				job.setProgressGroup(pg, IProgressMonitor.UNKNOWN);

			}
		}
	}
}
