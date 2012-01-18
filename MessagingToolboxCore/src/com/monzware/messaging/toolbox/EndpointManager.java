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

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IWorkingSetManager;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.XMLMemento;
import org.eclipse.ui.statushandlers.IStatusAdapterConstants;
import org.eclipse.ui.statushandlers.StatusAdapter;
import org.eclipse.ui.statushandlers.StatusManager;
import org.osgi.framework.BundleContext;

import com.monzware.messaging.toolbox.core.configmodel.Endpoint;
import com.monzware.messaging.toolbox.core.configmodel.impl.EndpointImpl;
import com.monzware.messaging.toolbox.core.configmodel.impl.EndpointSystemImpl;

/**
 * A working set manager stores working sets and provides property change
 * notification when a working set is added or removed. Working sets are
 * persisted whenever one is added or removed.
 * 
 * @see IWorkingSetManager
 * @since 2.0
 */
public class EndpointManager {

	private static final String ENDPOINTS_ELEMENT = "Endpoints";
	private static final String VENDOR_NAME_ELEMENT = "VendorName";
	private static final String VENDOR_PLUGINID_ELEMENT = "VendorPluginId";
	private static final String SERVERPORT_ELEMENT = "Port";
	private static final String SERVERNAME_ELEMENT = "ServerName";
	private static final String SYSTEMNAME_ELEMENT = "Name";
	private static final String SYSTEMID_ELEMENT = "ID";
	private static final String ENDPOINTNAME_ELEMENT = "Name";
	private static final String ENDPOINTID_ELEMENT = "Id";
	private static final String ENDPOINT_ELEMENT = "Endpoint";
	private static final String ENDPOINTSYSTEM_ELEMENT = "Endpointsystem";

	// Working set persistence
	public static final String WORKING_SET_STATE_FILENAME = "endpointconfiguration.xml"; //$NON-NLS-1$

	private boolean restoreInProgress;

	private boolean savePending;

	private Collection<EndpointSystemImpl> endpointSystems = new ArrayList<EndpointSystemImpl>();

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

				for (IMemento esMemento : memento.getChildren(ENDPOINTSYSTEM_ELEMENT)) {

					EndpointSystemImpl es = new EndpointSystemImpl();

					getEndpointSystems().add(es);

					Collection<Endpoint> eps = es.getEndpoints();

					es.setSystemId(esMemento.getString(SYSTEMID_ELEMENT));
					es.setSystemName(esMemento.getString(SYSTEMNAME_ELEMENT));

					es.setServerName(esMemento.getString(SERVERNAME_ELEMENT));
					es.setPortNumber(esMemento.getString(SERVERPORT_ELEMENT));

					es.setVendorId(esMemento.getString(VENDOR_PLUGINID_ELEMENT));
					es.setVendorName(esMemento.getString(VENDOR_NAME_ELEMENT));

					IMemento endpoint = esMemento.getChild(ENDPOINTS_ELEMENT);

					for (IMemento eMemento : endpoint.getChildren(ENDPOINT_ELEMENT)) {

						String epName = eMemento.getString(ENDPOINTNAME_ELEMENT);
						String ipId = eMemento.getString(ENDPOINTID_ELEMENT);

						EndpointImpl ep = new EndpointImpl(es, epName);
						ep.setId(ipId);

						eps.add(ep);

					}

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

		for (EndpointSystemImpl system : getEndpointSystems()) {

			IMemento endpointSystemMemento = memento.createChild(ENDPOINTSYSTEM_ELEMENT);

			endpointSystemMemento.putString(SYSTEMID_ELEMENT, system.getSystemId());
			endpointSystemMemento.putString(SYSTEMNAME_ELEMENT, system.getSystemName());

			endpointSystemMemento.putString(SERVERNAME_ELEMENT, system.getServerName());
			endpointSystemMemento.putString(SERVERPORT_ELEMENT, system.getPortNumber());

			endpointSystemMemento.putString(VENDOR_PLUGINID_ELEMENT, system.getVendorId());
			endpointSystemMemento.putString(VENDOR_NAME_ELEMENT, system.getVendorName());

			Collection<Endpoint> endpoints = system.getEndpoints();

			IMemento endpointsMementoChild = endpointSystemMemento.createChild(ENDPOINTS_ELEMENT);

			for (Endpoint endpoint : endpoints) {

				IMemento endpointMementoChild = endpointsMementoChild.createChild(ENDPOINT_ELEMENT);

				endpointMementoChild.putString(ENDPOINTNAME_ELEMENT, endpoint.getName());
				endpointMementoChild.putString(ENDPOINTID_ELEMENT, endpoint.getId());

			}

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

	public void addEndpointSystem(EndpointSystemImpl system) {
		getEndpointSystems().add(system);
	}

	public Collection<EndpointSystemImpl> getEndpointSystems() {
		return endpointSystems;
	}	
}
