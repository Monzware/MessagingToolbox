package com.monzware.messaging.ui.editor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import com.monzware.messaging.toolbox.core.configmodel.Endpoint;

public class EndpointEditor extends EditorPart {

	private EndpointEditorInput endPointEditor;
	private TableViewer viewer;

	public EndpointEditor() {

	}

	@Override
	public void doSave(IProgressMonitor monitor) {

	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {

		if (input instanceof EndpointEditorInput) {
			endPointEditor = (EndpointEditorInput) input;
			Endpoint ep = endPointEditor.getEndpoint();
			setPartName(ep.getEndpointsystem().getSystemName() + " -> " + ep.getName());
		}

		setSite(site);
		setInput(input);
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		viewer = new TableViewer(parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(new EndpointEditorContentProvider(endPointEditor.getEndpoint().getEndpointReceiver()));
		viewer.setLabelProvider(new EndpointEditorLabelProvider());
		viewer.setInput(getSite());
		viewer.getTable().setLinesVisible(true);		
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

}
