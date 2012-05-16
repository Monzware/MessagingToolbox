package com.monzware.messaging.toolbox.core.wizards;

import com.monzware.messaging.toolbox.core.configmodel.EndpointSystem;

public interface MessagingSystemWizardEditableExtention<T extends EndpointSystem> extends MessagingSystemWizardExtention<T> {

	public void setOldSystem(T system);
}
