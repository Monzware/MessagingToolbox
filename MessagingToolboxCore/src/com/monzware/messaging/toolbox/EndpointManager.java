package com.monzware.messaging.toolbox;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IWorkingSetManager;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.XMLMemento;
import org.eclipse.ui.statushandlers.IStatusAdapterConstants;
import org.eclipse.ui.statushandlers.StatusAdapter;
import org.eclipse.ui.statushandlers.StatusManager;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import com.monzware.messaging.toolbox.core.configmodel.EndpointSystem;
import com.monzware.messaging.toolbox.core.configmodel.EndpointsystemChangeListener;
import com.monzware.messaging.toolbox.core.configmodel.PersistenceConstants;
import com.monzware.messaging.toolbox.providers.ConfigurationPersistance;

/**
 * A working set manager stores working sets and provides property change
 * notification when a working set is added or removed. Working sets are
 * persisted whenever one is added or removed.
 * 
 * @see IWorkingSetManager
 * @since 2.0
 */
public class EndpointManager {

	// Working set persistence
	public static final String WORKING_SET_STATE_FILENAME = "endpointconfiguration.xml"; //$NON-NLS-1$

	private boolean restoreInProgress;

	private boolean savePending;

	private Collection<EndpointSystem> endpointSystems = new ArrayList<EndpointSystem>();

	private Collection<EndpointsystemChangeListener> endpointsystemChangeListeners = new ArrayList<EndpointsystemChangeListener>();

	public EndpointManager(BundleContext context) {

	}

	/**
	 * Returns the file used as the persistence store, or <code>null</code> if
	 * there is no available file.
	 * 
	 * @return the file used as the persistence store, or <code>null</code>
	 */
	private File getWorkingSetStateFile() {
		IPath path = MessagingToolboxPlugin.getDefault().getDataLocation();
		if (path == null) {
			return null;
		}
		path = path.append(WORKING_SET_STATE_FILENAME);
		return path.toFile();
	}

	/**
	 * Reads the persistence store and creates the working sets stored in it.
	 */
	public void restoreState() {
		File stateFile = getWorkingSetStateFile();

		if (stateFile != null && stateFile.exists()) {
			try {
				restoreInProgress = true;

				FileInputStream input = new FileInputStream(stateFile);
				BufferedReader reader = new BufferedReader(new InputStreamReader(input, "utf-8")); //$NON-NLS-1$

				IMemento memento = XMLMemento.createReadRoot(reader);

				for (IMemento esMemento : memento.getChildren(PersistenceConstants.ENDPOINTSYSTEM_ELEMENT)) {

					String pluginId = esMemento.getString(PersistenceConstants.PROVIDER_PLUGINID_ELEMENT);
					String systemName = esMemento.getString(PersistenceConstants.SYSTEMNAME_ELEMENT);

					ConfigurationPersistance cp = getConfigurationPersitence(pluginId);

					EndpointSystem es = cp.getEndpointSystemFromConfiguration(pluginId, systemName, esMemento);
					getEndpointSystems().add(es);
				}

				reader.close();
			} catch (IOException e) {
				handleInternalError(e, "", "");
			} catch (WorkbenchException e) {
				handleInternalError(e, "", "");
			} finally {
				restoreInProgress = false;
			}

			if (savePending) {
				saveState();
				savePending = false;
			}
		}
	}

	/**
	 * Saves the working sets in the persistence store
	 */
	public void saveState() {
		if (restoreInProgress) {
			// bug 327396: avoid saving partial state
			savePending = true;
			return;
		}

		File stateFile = getWorkingSetStateFile();
		if (stateFile == null) {
			return;
		}
		try {
			saveState(stateFile);
		} catch (IOException e) {
			stateFile.delete();
			handleInternalError(e, "", "");
		}
	}

	/**
	 * Save the state to the state file.
	 * 
	 * @param stateFile
	 * @throws IOException
	 */
	private void saveState(File stateFile) throws IOException {
		XMLMemento memento = XMLMemento.createWriteRoot("EndpointConfiguration");

		saveEndpointSystems(memento);

		FileOutputStream stream = new FileOutputStream(stateFile);
		OutputStreamWriter writer = new OutputStreamWriter(stream, "utf-8"); //$NON-NLS-1$
		memento.save(writer);
		writer.close();
	}

	private void saveEndpointSystems(XMLMemento memento) {

		for (EndpointSystem system : getEndpointSystems()) {

			IMemento endpointSystemMemento = memento.createChild(PersistenceConstants.ENDPOINTSYSTEM_ELEMENT);

			ConfigurationPersistance cp = getConfigurationPersitence(system.getProviderId());
			cp.addEndpointSystemToConfiguration(system, endpointSystemMemento);
		}
	}

	/**
	 * Show and Log the exception using StatusManager.
	 */
	private void handleInternalError(Exception exp, String title, String message) {
		Status status = new Status(IStatus.ERROR, "", message, exp);
		StatusAdapter sa = new StatusAdapter(status);
		sa.setProperty(IStatusAdapterConstants.TITLE_PROPERTY, title);
		StatusManager.getManager().handle(sa, StatusManager.LOG);
	}

	public void addEndpointSystem(EndpointSystem system) {
		getEndpointSystems().add(system);

		for (EndpointsystemChangeListener l : endpointsystemChangeListeners) {
			l.endPointAdded(system);
		}
	}

	public void deleteEndpointSystem(EndpointSystem system) {
		getEndpointSystems().remove(system);

		for (EndpointsystemChangeListener l : endpointsystemChangeListeners) {
			l.endPointDeleted(system);
		}
	}

	public Collection<EndpointSystem> getEndpointSystems() {
		return endpointSystems;
	}

	private ConfigurationPersistance getConfigurationPersitence(String pluginId) {

		try {
			IExtensionRegistry registry = Platform.getExtensionRegistry();

			IExtension extension = registry.getExtension("com.monzware.messaging.providers", pluginId);

			if (extension != null) {

				String pluginName = extension.getContributor().getName();
				Bundle bundle = Platform.getBundle(pluginName);

				for (IConfigurationElement iConfigurationElement : extension.getConfigurationElements()) {

					if (iConfigurationElement.getName().equals("configurationpersitence")) {

						String clazz = iConfigurationElement.getAttribute("class");
						Class<?> loadClass = bundle.loadClass(clazz);

						return (ConfigurationPersistance) loadClass.newInstance();
					}
				}
			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;

	}

	public void addEndpointsystemChangeListener(EndpointsystemChangeListener endpointsystemChangeListener) {
		endpointsystemChangeListeners.add(endpointsystemChangeListener);
	}
}
