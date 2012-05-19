package com.monzware.messaging.toolbox.core.popup.actions;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorPart;

import com.monzware.messaging.toolbox.MessagingToolboxPlugin;
import com.monzware.messaging.ui.editor.EndpointEditor;

public class UpdateEndpointAction extends Action {

	private ImageDescriptor image = ImageDescriptor.createFromURL(FileLocator.find(MessagingToolboxPlugin.getDefault().getBundle(), new Path("/icons/refresh.gif"), null));
	private IEditorPart targetEditor;

	public UpdateEndpointAction() {
		super("Update endpont");
		setToolTipText("Update the content of the endpoint");
		setImageDescriptor(image);
	}

	@Override
	public void run() {

		/*
		 * IWorkbench workbench = PlatformUI.getWorkbench(); IWorkbenchWindow ww
		 * = workbench.getActiveWorkbenchWindow(); IWorkbenchPart activePart =
		 * ww.getPartService().getActivePart();
		 */

		if (targetEditor != null && targetEditor instanceof EndpointEditor) {
			EndpointEditor editor = (EndpointEditor) targetEditor;
			editor.refreshContent();
		}
	}

	public void setActiveEditor(IEditorPart targetEditor) {
		this.targetEditor = targetEditor;

	}

}
