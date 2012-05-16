package com.monzware.messaging.toolbox.core.wizards;

import com.monzware.messaging.toolbox.core.configmodel.EndpointSystem;

public interface MessagingSystemWizardExtention<T extends EndpointSystem> {

	public void setNewSystem(T system);

	public void updateNewSystem();

}
