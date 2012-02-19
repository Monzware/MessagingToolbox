package com.monzware.messaging.job;

import org.eclipse.core.runtime.IStatus;

import com.monzware.messaging.toolbox.MessagingToolboxPlugin;

public class SendMessageJobErrorStatus implements IStatus {

	private final Exception exception;

	public SendMessageJobErrorStatus(Exception exception) {
		this.exception = exception;
	}

	public IStatus[] getChildren() {
		return new IStatus[0];
	}

	public int getCode() {
		return 0;
	}

	public Throwable getException() {
		return exception;
	}

	public String getMessage() {
		return exception.getMessage();
	}

	public String getPlugin() {
		return MessagingToolboxPlugin.PLUGIN_ID;
	}

	public int getSeverity() {
		return ERROR;
	}

	public boolean isMultiStatus() {
		return false;
	}

	public boolean isOK() {
		return false;
	}

	public boolean matches(int severityMask) {
		return getSeverity() == severityMask;
	}

}
