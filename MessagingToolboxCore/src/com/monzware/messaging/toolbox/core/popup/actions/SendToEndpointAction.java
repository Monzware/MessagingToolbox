package com.monzware.messaging.toolbox.core.popup.actions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import com.monzware.messaging.toolbox.core.configmodel.Endpoint;
import com.monzware.messaging.toolbox.core.configmodel.EndpointSender;

public class SendToEndpointAction extends Action {

	private final Endpoint endpoint;

	public SendToEndpointAction(Endpoint endpoint) {
		super(endpoint.getName());
		this.endpoint = endpoint;
	}

	@Override
	public void runWithEvent(Event event) {

		IWorkbench workbench = PlatformUI.getWorkbench();
		ISelectionService selectionService = workbench.getActiveWorkbenchWindow().getSelectionService();
		StructuredSelection selection = (StructuredSelection) selectionService.getSelection();

		@SuppressWarnings("unchecked")
		List<IFile> list = selection.toList();
		for (IFile iFile : list) {

			try {

				EndpointSender endpointSender = endpoint.getEndpointSender();
				if (endpointSender != null) {

					InputStream contents = iFile.getContents();
					String message = convertStreamToString(contents);					
					endpointSender.sendMessage(message);
				}

			} catch (CoreException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public String convertStreamToString(InputStream is) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line + "\n");
		}
		is.close();
		return sb.toString();
	}

}
