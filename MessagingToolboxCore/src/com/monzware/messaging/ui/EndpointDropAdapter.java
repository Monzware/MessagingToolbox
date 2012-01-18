package com.monzware.messaging.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TransferData;

import com.monzware.messaging.toolbox.core.configmodel.Endpoint;
import com.monzware.messaging.toolbox.core.configmodel.EndpointSender;
import com.monzware.messaging.toolbox.util.InputStreamToStringConverter;

public class EndpointDropAdapter extends ViewerDropAdapter {

	// private final TreeViewer viewer;

	public EndpointDropAdapter(TreeViewer viewer) {
		super(viewer);
		// this.viewer = viewer;
	}

	@Override
	public boolean validateDrop(Object target, int operation, TransferData transferType) {

		DropTargetEvent event = getCurrentEvent();
		int location = determineLocation(event);

		return target instanceof Endpoint && location == LOCATION_ON;
	}

	@Override
	public boolean performDrop(Object data) {

		Object target = getCurrentTarget();
		if (target instanceof Endpoint) {
			Endpoint endpoint = (Endpoint) target;

			if (data instanceof String) {
				String dataString = (String) data;
				return addStringToEndpoint(dataString, endpoint);

			} else if (data instanceof String[]) {
				String[] files = (String[]) data;
				return addFileToEndpoint(files, endpoint);
			}
		}
		return false;
	}

	private boolean addFileToEndpoint(String[] files, Endpoint endpoint) {
		boolean result = false;

		for (String string : files) {
			File file = new File(string);
			if (file.exists()) {

				try {
					FileInputStream is = new FileInputStream(file);
					InputStreamToStringConverter conv = new InputStreamToStringConverter(is);
					boolean addStringToEndpoint = addStringToEndpoint(conv.getString(), endpoint);
					if (addStringToEndpoint) {
						result = true;
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	private boolean addStringToEndpoint(String dataString, Endpoint endpoint) {
		EndpointSender sender = endpoint.getEndpointSender();
		if (sender != null) {
			sender.sendMessage(dataString);
			return true;
		}
		return false;
	}

}
