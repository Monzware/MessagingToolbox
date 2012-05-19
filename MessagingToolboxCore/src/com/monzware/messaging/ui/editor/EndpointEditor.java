package com.monzware.messaging.ui.editor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
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
		viewer.getTable().setLinesVisible(true);
		viewer.getTable().setHeaderVisible(true);

		TableColumn tc1 = new TableColumn(viewer.getTable(), SWT.LEFT, 0);
		tc1.setText("Id");
		tc1.setWidth(300);

		TableColumn tc2 = new TableColumn(viewer.getTable(), SWT.LEFT, 1);
		tc2.setText("Timestamp");
		tc2.setWidth(120);

		TableColumn tc3 = new TableColumn(viewer.getTable(), SWT.LEFT, 2);
		tc3.setText("Message");
		tc3.setWidth(500);

		Image image = new Image(parent.getDisplay(), getClass().getResourceAsStream("/icons/database2.png"));
		setTitleImage(image);

		viewer.setInput(getSite());
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	public void refreshContent() {
		viewer.refresh();
	}

}
