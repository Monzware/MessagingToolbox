package com.monzware.messaging.ui.editor;

import java.util.Collection;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.IElementFactory;
import org.eclipse.ui.IMemento;

import com.monzware.messaging.toolbox.EndpointManager;
import com.monzware.messaging.toolbox.MessagingToolboxPlugin;
import com.monzware.messaging.toolbox.core.configmodel.Endpoint;
import com.monzware.messaging.toolbox.core.configmodel.EndpointSystem;

/**
 * Factory for saving and restoring a <code>FileEditorInput</code>. The stored
 * representation of a <code>FileEditorInput</code> remembers the full path of
 * the file (that is, <code>IFile.getFullPath</code>).
 * <p>
 * The workbench will automatically create instances of this class as required.
 * It is not intended to be instantiated or subclassed by the client.
 * </p>
 * 
 * @noinstantiate This class is not intended to be instantiated by clients.
 * @noextend This class is not intended to be subclassed by clients.
 */
public class EndpointEditorInputFactory implements IElementFactory {
	/**
	 * Factory id. The workbench plug-in registers a factory by this name with
	 * the "org.eclipse.ui.elementFactories" extension point.
	 */
	private static final String ID_FACTORY = "com.monzware.messaging.ui.MessagingToolboxEndpointEditorFactory"; //$NON-NLS-1$

	/**
	 * Tag the system name
	 */
	private static final String TAG_SYSTEM_NAME = "SystemName";

	/**
	 * Tag the endpoint name
	 */
	private static final String TAG_ENDPOINT_NAME = "EndpointName";

	/**
	 * Creates a new factory.
	 */
	public EndpointEditorInputFactory() {
	}

	/*
	 * (non-Javadoc) Method declared on IElementFactory.
	 */
	public IAdaptable createElement(IMemento memento) {

		String endpointName = memento.getString(TAG_ENDPOINT_NAME);
		String systemName = memento.getString(TAG_SYSTEM_NAME);

		if (endpointName != null && systemName != null) {

			EndpointManager epm = MessagingToolboxPlugin.getDefault().getEndpointManager();
			Collection<EndpointSystem> endpointSystems = epm.getEndpointSystems();
			for (EndpointSystem endpointSystem : endpointSystems) {
				if (endpointSystem.getSystemName().equals(systemName)) {
					Collection<? extends Endpoint> endpoints = endpointSystem.getEndpoints();
					for (Endpoint endpoint : endpoints) {
						if (endpoint.getName().equals(endpointName)) {
							return new EndpointEditorInput(endpoint);
						}
					}
				}
			}
		}

		return null;
	}

	/**
	 * Returns the element factory id for this class.
	 * 
	 * @return the element factory id
	 */
	public static String getFactoryId() {
		return ID_FACTORY;
	}

	/**
	 * Saves the state of the given file editor input into the given memento.
	 * 
	 * @param memento
	 *            the storage area for element state
	 * @param input
	 *            the file editor input
	 */
	public static void saveState(IMemento memento, EndpointEditorInput input) {

		Endpoint endpoint = input.getEndpoint();

		memento.putString(TAG_ENDPOINT_NAME, endpoint.getName());
		memento.putString(TAG_SYSTEM_NAME, endpoint.getEndpointsystem().getSystemName());
	}
}
