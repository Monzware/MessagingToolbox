package com.monzware.messaging.toolbox.core.popup.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.monzware.messaging.toolbox.core.configmodel.Endpoint;
import com.monzware.messaging.toolbox.core.configmodel.EndpointReceiver;
import com.monzware.messaging.toolbox.core.configmodel.EndpointReceiverException;
import com.monzware.messaging.ui.editor.EndpointEditor;
import com.monzware.messaging.ui.editor.EndpointEditorInput;

public class SaveAllMessagesFromEndpointEditorAction extends Action {

	private IEditorPart targetEditor;

	public SaveAllMessagesFromEndpointEditorAction() {
		super("Save all messages to files");
		setToolTipText("Save all messages from the endpoint to files");
		
		ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
		setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_ETOOL_SAVEALL_EDIT));
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
