package com.monzware.messaging.ui;

import java.io.File;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.IJobChangeListener;
import org.eclipse.core.runtime.jobs.IJobManager;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TransferData;

import com.monzware.messaging.job.SendMessageFromFileJob;
import com.monzware.messaging.job.SendMessageJob;
import com.monzware.messaging.job.SendMessageJobChangeListener;
import com.monzware.messaging.toolbox.core.configmodel.Endpoint;

public class EndpointDropAdapter extends ViewerDropAdapter {

	private TreeViewer viewer;

	public EndpointDropAdapter(TreeViewer viewer) {
		super(viewer);
		this.viewer = viewer;
	}

	public boolean validateDrop(Object target, int operation, TransferData transferType) {

		DropTargetEvent event = getCurrentEvent();
		int location = determineLocation(event);

		return target instanceof Endpoint && location == LOCATION_ON;
	}

	public boolean performDrop(Object data) {

		Object target = getCurrentTarget();
		if (target instanceof Endpoint) {
			Endpoint endpoint = (Endpoint) target;

			if (data instanceof String) {
				String dataString = (String) data;
				return addStringToEndpoint(dataString, endpoint);

			} else if (data instanceof String[]) {
				String[] files = (String[]) data;
				return addFilesToEndpoint(files, endpoint);
			}
		}
		return false;
	}

	private boolean addFilesToEndpoint(String[] files, Endpoint endpoint) {
		boolean result = false;

		IJobManager jm = Job.getJobManager();
		IProgressMonitor pg = jm.createProgressGroup();
		pg.setTaskName("Add files content to endpoint");

		for (String string : files) {
			File file = new File(string);
			if (file.exists()) {

				SendMessageFromFileJob job = new SendMessageFromFileJob(endpoint, file);
				IJobChangeListener listener = new SendMessageJobChangeListener(viewer.getControl().getShell());
				job.addJobChangeListener(listener);
				job.setUser(true);
				job.setPriority(SendMessageJob.SHORT);
				job.schedule();
				job.setProgressGroup(pg, IProgressMonitor.UNKNOWN);

				result = true;
			}
		}
		return result;
	}

	private boolean addStringToEndpoint(String dataString, Endpoint endpoint) {

		SendMessageJob job = new SendMessageJob(endpoint, dataString);
		IJobChangeListener listener = new SendMessageJobChangeListener(viewer.getControl().getShell());
		job.addJobChangeListener(listener);
		job.setUser(true);
		job.setPriority(SendMessageJob.SHORT);
		job.schedule();

		return true;
	}
}
