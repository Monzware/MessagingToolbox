package com.monzware.messaging.toolbox.core.popup.actions;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

public class SendToEndpointAction extends Action {

	public SendToEndpointAction(String string) {
		super(string);
	}

	@Override
	public void runWithEvent(Event event) {

		IWorkbench workbench = PlatformUI.getWorkbench();
		ISelectionService selectionService = workbench.getActiveWorkbenchWindow().getSelectionService();
		StructuredSelection selection = (StructuredSelection) selectionService.getSelection();

		@SuppressWarnings("unchecked")
		List<IFile> list = selection.toList();

		System.out.println(list);

	}

}
