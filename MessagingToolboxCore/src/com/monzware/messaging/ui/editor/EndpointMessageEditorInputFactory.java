package com.monzware.messaging.ui.editor;

import java.util.Collection;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.IElementFactory;
import org.eclipse.ui.IMemento;

import com.monzware.messaging.toolbox.EndpointManager;
import com.monzware.messaging.toolbox.MessagingToolboxPlugin;
import com.monzware.messaging.toolbox.core.configmodel.Endpoint;
import com.monzware.messaging.toolbox.core.configmodel.EndpointReceiver;
import com.monzware.messaging.toolbox.core.configmodel.EndpointReceiverException;
import com.monzware.messaging.toolbox.core.configmodel.EndpointReceiverMessage;
import com.monzware.messaging.toolbox.core.configmodel.EndpointSystem;

public class EndpointMessageEditorInputFactory implements IElementFactory {
	/**
	 * Factory id. The workbench plug-in registers a factory by this name with
	 * the "org.eclipse.ui.elementFactories" extension point.
	 */
	private static final String ID_FACTORY = "com.monzware.messaging.ui.MessagingToolboxEndpointMessageEditorFactory"; //$NON-NLS-1$

	/**
	 * Tag the system name
	 */
	private static final String TAG_SYSTEM_NAME = "SystemName";

	/**
	 * Tag the endpoint name
	 */
	private static final String TAG_ENDPOINT_NAME = "EndpointName";

	/**
	 * Tag the messageID name
	 */
	private static final String TAG_MESSAGE_ID = "MessageID";

	/**
	 * Creates a new factory.
	 */
	public EndpointMessageEditorInputFactory() {
	}

	/*
	 * (non-Javadoc) Method declared on IElementFactory.
	 */
	public IAdaptable createElement(IMemento memento) {

		String endpointName = memento.getString(TAG_ENDPOINT_NAME);
		String systemName = memento.getString(TAG_SYSTEM_NAME);
		String messageId = memento.getString(TAG_MESSAGE_ID);

		if (endpointName != null && systemName != null) {

			EndpointManager epm = MessagingToolboxPlugin.getDefault().getEndpointManager();
			Collection<EndpointSystem> endpointSystems = epm.getEndpointSystems();
			for (EndpointSystem endpointSystem : endpointSystems) {
				if (endpointSystem.getSystemName().equals(systemName)) {
					Collection<? extends Endpoint> endpoints = endpointSystem.getEndpoints();
					for (Endpoint endpoint : endpoints) {
						if (endpoint.getName().equals(endpointName)) {
							EndpointReceiver epr = endpoint.getEndpointReceiver();
							try {
								Collection<EndpointReceiverMessage> messages = epr.getMessages();

								for (EndpointReceiverMessage message : messages) {
									if (messageId.equals(message.getMessageId())) {
										return new EndpointMessageEditorInput(endpoint, message);
									}

								}
							} catch (EndpointReceiverException e) {
								return new EndpointMessageEditorInput(endpoint, null);
							}
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
	public static void saveState(IMemento memento, EndpointMessageEditorInput input) {

		Endpoint endpoint = input.getEndpoint();
		EndpointReceiverMessage message = input.getMessage();
		
		if(message != null) {
			memento.putString(TAG_ENDPOINT_NAME, endpoint.getName());
			memento.putString(TAG_SYSTEM_NAME, endpoint.getEndpointsystem().getSystemName());
			memento.putString(TAG_MESSAGE_ID, message.getMessageId());
		}
	}
}
