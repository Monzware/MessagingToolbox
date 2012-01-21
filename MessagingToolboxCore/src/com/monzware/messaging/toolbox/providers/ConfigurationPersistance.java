package com.monzware.messaging.toolbox.providers;

import org.eclipse.ui.IMemento;

import com.monzware.messaging.toolbox.core.configmodel.EndpointSystem;

public interface ConfigurationPersistance {

	EndpointSystem getEndpointSystemFromConfiguration(String pluginId, String systemName, IMemento esMemento);
	
	void addEndpointSystemToConfiguration(EndpointSystem system, IMemento endpointSystemMemento);

}
