package com.monzware.messaging.job;

import org.eclipse.core.runtime.IStatus;

import com.monzware.messaging.toolbox.MessagingToolboxPlugin;

public class SendMessageJobErrorStatus implements IStatus {

	private final Exception exception;

	public SendMessageJobErrorStatus(Exception exception) {
		this.exception = exception;
	}

	@Override
	public IStatus[] getChildren() {
		return new IStatus[0];
	}

	@Override
	public int getCode() {
		return 0;
	}

	@Override
	public Throwable getException() {
		return exception;
	}

	@Override
	public String getMessage() {
		return exception.getMessage();
	}

	@Override
	public String getPlugin() {
		return MessagingToolboxPlugin.PLUGIN_ID;
	}

	@Override
	public int getSeverity() {
		return ERROR;
	}

	@Override
	public boolean isMultiStatus() {
		return false;
	}

	@Override
	public boolean isOK() {
		return false;
	}

	@Override
	public boolean matches(int severityMask) {
		return getSeverity() == severityMask;
	}

}
