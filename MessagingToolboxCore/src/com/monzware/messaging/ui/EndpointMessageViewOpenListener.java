package com.monzware.messaging.ui;

import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.monzware.messaging.toolbox.core.configmodel.Endpoint;
import com.monzware.messaging.toolbox.core.configmodel.EndpointReceiverMessage;
import com.monzware.messaging.ui.editor.EndpointEditorContentProvider;
import com.monzware.messaging.ui.editor.EndpointMessageEditorInput;

public class EndpointMessageViewOpenListener implements IOpenListener {

	public void open(OpenEvent event) {
		StructuredSelection selection = (StructuredSelection) event.getSelection();

		Object firstElement = selection.getFirstElement();

		if (firstElement instanceof EndpointReceiverMessage) {

			EndpointReceiverMessage message = (EndpointReceiverMessage) firstElement;

			TableViewer source = (TableViewer) event.getSource();
			EndpointEditorContentProvider contentProvider = (EndpointEditorContentProvider) source.getContentProvider();

			Endpoint endpoint = contentProvider.getEndpoint();

			IWorkbench wb = PlatformUI.getWorkbench();
			IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
			IWorkbenchPage page = win.getActivePage();

			try {
				page.openEditor(new EndpointMessageEditorInput(endpoint, message), "com.monzware.messaging.ui.MessagingToolboxEndpointContentEditor");
			} catch (PartInitException e) {
				e.printStackTrace();
			}
		}
	}
}
