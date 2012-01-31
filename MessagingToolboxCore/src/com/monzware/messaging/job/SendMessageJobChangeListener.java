package com.monzware.messaging.job;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public class SendMessageJobChangeListener extends JobChangeAdapter {

	private final Shell shell;

	public SendMessageJobChangeListener(Shell shell) {
		this.shell = shell;
	}

	@Override
	public void done(final IJobChangeEvent event) {

		shell.getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {

				IStatus resultStatus = event.getResult();

				EndpointJob job = (EndpointJob) event.getJob();

				Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
				if (resultStatus.getSeverity() == IStatus.ERROR) {
					MessageDialog.openError(shell, "Unable to deliver message", "Unable to deliver message to:\r\n" + job.getEndpointName() + "\r\n\r\n" + resultStatus.getMessage());
				} else if (resultStatus.getSeverity() == IStatus.OK) {

				}
			}
		});
	}
}
