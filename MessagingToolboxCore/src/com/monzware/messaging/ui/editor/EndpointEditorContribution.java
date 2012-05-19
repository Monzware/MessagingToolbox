package com.monzware.messaging.ui.editor;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.EditorActionBarContributor;

import com.monzware.messaging.toolbox.core.popup.actions.UpdateEndpointAction;

public class EndpointEditorContribution extends EditorActionBarContributor {

	private UpdateEndpointAction updateEndpontAction;

	public EndpointEditorContribution() {
		super();
		createActions();
		
	}

	@Override
	public void setActiveEditor(IEditorPart targetEditor) {
		updateEndpontAction.setActiveEditor(targetEditor);
	}

	private void createActions() {
		updateEndpontAction = new UpdateEndpointAction();
	}

	public void contributeToMenu(IMenuManager manager) {
		IMenuManager menu = new MenuManager("Messaging toolbox");
		manager.prependToGroup(IWorkbenchActionConstants.MB_ADDITIONS, menu);
		menu.add(updateEndpontAction);
	}

	public void contributeToToolBar(IToolBarManager manager) {
		manager.add(new Separator());
		manager.add(updateEndpontAction);
	}

}
