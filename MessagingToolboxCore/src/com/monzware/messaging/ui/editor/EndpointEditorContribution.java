package com.monzware.messaging.ui.editor;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.EditorActionBarContributor;

import com.monzware.messaging.toolbox.core.popup.actions.DeleteAllMessagesFromEndpointEditorAction;
import com.monzware.messaging.toolbox.core.popup.actions.SaveAllMessagesFromEndpointEditorAction;
import com.monzware.messaging.toolbox.core.popup.actions.SaveSelectedMessageFromEndpointEditorAction;
import com.monzware.messaging.toolbox.core.popup.actions.UpdateEndpointEditorAction;

public class EndpointEditorContribution extends EditorActionBarContributor {

	private UpdateEndpointEditorAction updateEndpontAction;
	private DeleteAllMessagesFromEndpointEditorAction deleteAction;
	//private SaveSelectedMessageFromEndpointEditorAction saveAction;
	//private SaveAllMessagesFromEndpointEditorAction saveAllAction;

	public EndpointEditorContribution() {
		super();
		createActions();
	}

	@Override
	public void setActiveEditor(IEditorPart targetEditor) {
		updateEndpontAction.setActiveEditor(targetEditor);
		deleteAction.setActiveEditor(targetEditor);
	}

	private void createActions() {
		updateEndpontAction = new UpdateEndpointEditorAction();
		deleteAction = new DeleteAllMessagesFromEndpointEditorAction();
		//saveAction = new SaveSelectedMessageFromEndpointEditorAction();
		//saveAllAction = new SaveAllMessagesFromEndpointEditorAction();
	}

	public void contributeToMenu(IMenuManager manager) {
		IMenuManager menu = new MenuManager("Messaging toolbox");
		manager.prependToGroup(IWorkbenchActionConstants.MB_ADDITIONS, menu);
		menu.add(updateEndpontAction);
		menu.add(deleteAction);
		//menu.add(saveAction);
		//menu.add(saveAllAction);
	}

	public void contributeToToolBar(IToolBarManager manager) {
		manager.add(new Separator());
		manager.add(updateEndpontAction);
		manager.add(deleteAction);
		//manager.add(saveAction);
		//manager.add(saveAllAction);
	}
}
