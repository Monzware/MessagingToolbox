package com.monzware.messaging.ui;

import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.monzware.messaging.toolbox.core.configmodel.Endpoint;
import com.monzware.messaging.ui.editor.EndpointEditorInput;

public class EndpointTreeViewOpenListener implements IOpenListener {

	public void open(OpenEvent event) {
		TreeSelection selection = (TreeSelection) event.getSelection();
		Object firstElement = selection.getFirstElement();

		if (firstElement instanceof Endpoint) {
			Endpoint endpoint = (Endpoint) firstElement;
			// System.out.println(endpoint.getName());

			IWorkbench wb = PlatformUI.getWorkbench();
			IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
			IWorkbenchPage page = win.getActivePage();

			try {
				page.openEditor(new EndpointEditorInput(endpoint), "com.monzware.messaging.ui.MessagingToolboxEndpointEditor");
			} catch (PartInitException e) {
				e.printStackTrace();
			}
		}
	}
}
