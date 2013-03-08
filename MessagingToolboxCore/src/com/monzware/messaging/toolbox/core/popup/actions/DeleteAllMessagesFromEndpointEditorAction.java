package com.monzware.messaging.toolbox.core.popup.actions;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;

import com.monzware.messaging.toolbox.MessagingToolboxPlugin;
import com.monzware.messaging.toolbox.core.configmodel.Endpoint;
import com.monzware.messaging.toolbox.core.configmodel.EndpointReceiver;
import com.monzware.messaging.toolbox.core.configmodel.EndpointReceiverException;
import com.monzware.messaging.ui.editor.EndpointEditor;
import com.monzware.messaging.ui.editor.EndpointEditorInput;

public class DeleteAllMessagesFromEndpointEditorAction extends Action {

	private ImageDescriptor image = ImageDescriptor.createFromURL(FileLocator.find(MessagingToolboxPlugin.getDefault().getBundle(), new Path("/icons/del.png"), null));
	private IEditorPart targetEditor;

	public DeleteAllMessagesFromEndpointEditorAction() {
		super("Delete all messages");
		setToolTipText("Delete all messages from the endpoint");
		setImageDescriptor(image);
	}

	public void runWithEvent(Event event) {

		try {
			if (targetEditor != null && targetEditor instanceof EndpointEditor) {
				EndpointEditor editor = (EndpointEditor) targetEditor;
				IEditorInput editorInput = editor.getEditorInput();
				if (editorInput instanceof EndpointEditorInput) {
					EndpointEditorInput endPointEditorInput = (EndpointEditorInput) editorInput;
					Endpoint ep = endPointEditorInput.getEndpoint();

					EndpointReceiver epr = ep.getEndpointReceiver();
					if (epr != null) {
						epr.clear();
						editor.refreshContent();
					}
				}
			}
		} catch (EndpointReceiverException e) {
			e.printStackTrace();
		}
	}

	public void setActiveEditor(IEditorPart targetEditor) {
		this.targetEditor = targetEditor;
	}
}
