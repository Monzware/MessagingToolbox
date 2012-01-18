package com.monzware.messaging.ui;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.DrillDownAdapter;
import org.eclipse.ui.part.ViewPart;

import com.monzware.messaging.toolbox.core.wizards.MessagingSystemWizard;

public class EndpointTreeView extends ViewPart {

	private Action addServerAction;
	private Shell shell;
	private Action editServerAction;
	private Action removeServerAction;
	private TreeViewer viewer;
	private DrillDownAdapter drillDownAdapter;

	public EndpointTreeView() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(Composite parent) {

		shell = parent.getShell();

		viewer = new TreeViewer(parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL);
		drillDownAdapter = new DrillDownAdapter(viewer);
		viewer.setContentProvider(new ViewContentProvider(this));
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setSorter(new NameSorter());
		viewer.setInput(getViewSite());

		int ops = DND.DROP_COPY | DND.DROP_MOVE;
		Transfer[] transfers = new Transfer[] { FileTransfer.getInstance(), TextTransfer.getInstance() };
		viewer.addDropSupport(ops, transfers, new EndpointDropAdapter(viewer));

		makeActions();
		contributeToActionBars();

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalToolBar(IToolBarManager manager) {

		// manager.add(addServerAction);
		// manager.add(editServerAction);
		manager.add(removeServerAction);

	}

	private void makeActions() {

		addServerAction = new Action() {
			public void run() {
				MessagingSystemWizard wizard = new MessagingSystemWizard();

				wizard.addPages();
				wizard.init(PlatformUI.getWorkbench(), null);
				WizardDialog dialog = new WizardDialog(shell, wizard);

				dialog.open();
			}
		};

		addServerAction.setEnabled(true);
		addServerAction.setText("Add server");
		addServerAction.setToolTipText("Add server");
		addServerAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ADD));

		editServerAction = new Action() {
			public void run() {
				MessagingSystemWizard wizard = new MessagingSystemWizard();
				wizard.addPages();
				wizard.init(PlatformUI.getWorkbench(), null);
				WizardDialog dialog = new WizardDialog(shell, wizard);

				dialog.open();
			}
		};

		editServerAction.setEnabled(true);
		editServerAction.setText("Change server");
		editServerAction.setToolTipText("Change server");
		editServerAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));

		removeServerAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection) selection).getFirstElement();

			}
		};

		removeServerAction.setEnabled(true);
		removeServerAction.setText("Remove server");
		removeServerAction.setToolTipText("Remove server");
		removeServerAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));

	}

}
