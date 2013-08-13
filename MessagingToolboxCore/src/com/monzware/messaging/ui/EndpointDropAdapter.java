package com.monzware.messaging.ui;

import java.io.File;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.IJobChangeListener;
import org.eclipse.core.runtime.jobs.IJobManager;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.TransferData;

import com.monzware.messaging.job.SendMessageFromFileJob;
import com.monzware.messaging.job.SendMessageFromIFileJob;
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

		DropTargetEvent event = getCurrentEvent();

		Object target = getCurrentTarget();
		if (target instanceof Endpoint) {
			Endpoint endpoint = (Endpoint) target;

			if (LocalSelectionTransfer.getTransfer().isSupportedType(event.currentDataType)) {
				ISelection sel = LocalSelectionTransfer.getTransfer().getSelection();
				return addSelectionsToEndpoint(sel, endpoint);

			} else if (TextTransfer.getInstance().isSupportedType(event.currentDataType)) {
				String dataString = (String) data;
				return addStringToEndpoint(dataString, endpoint);

			} else if (FileTransfer.getInstance().isSupportedType(event.currentDataType) && data instanceof String[]) {
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

		for (String fileName : files) {

			File file = new File(fileName);
			if (file.exists()) {

				Job job = new SendMessageFromFileJob(endpoint, file);
				IJobChangeListener listener = new SendMessageJobChangeListener(viewer.getControl().getShell());
				job.addJobChangeListener(listener);
				job.setUser(true);
				job.setPriority(SendMessageJob.SHORT);
				job.setProgressGroup(pg, IProgressMonitor.UNKNOWN);
				job.schedule();				

				result = true;
			}

		}
		return result;
	}

	private boolean addStringToEndpoint(String dataString, Endpoint endpoint) {

		Job job = new SendMessageJob(endpoint, dataString);
		IJobChangeListener listener = new SendMessageJobChangeListener(viewer.getControl().getShell());
		job.addJobChangeListener(listener);
		job.setUser(true);
		job.setPriority(SendMessageJob.SHORT);
		job.schedule();

		return true;
	}

	private boolean addSelectionsToEndpoint(ISelection sel, Endpoint endpoint) {

		if (sel instanceof StructuredSelection) {

			boolean result = false;
			IJobManager jm = Job.getJobManager();
			IProgressMonitor pg = jm.createProgressGroup();
			pg.setTaskName("Add files content to endpoint");

			StructuredSelection selection = (StructuredSelection) sel;

			List<?> list = selection.toList();
			for (Object element : list) {

				if (element instanceof IFile) {
					IFile file = (IFile) element;
					Job job = new SendMessageFromIFileJob(endpoint, file);
					IJobChangeListener listener = new SendMessageJobChangeListener(viewer.getControl().getShell());
					job.addJobChangeListener(listener);
					job.setUser(true);
					job.setPriority(SendMessageJob.SHORT);
					job.setProgressGroup(pg, IProgressMonitor.UNKNOWN);
					job.schedule();					
					result = true;
				}
			}

			return result;
		}

		return false;
	}
}
