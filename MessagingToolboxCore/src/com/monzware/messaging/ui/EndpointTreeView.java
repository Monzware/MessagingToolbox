package com.monzware.messaging.ui;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.monzware.messaging.toolbox.core.wizards.MessagingSystemWizard;

public class EndpointTreeView extends ViewPart {

	private Action addServerAction;
	private Shell shell;
	private Action editServerAction;

	public EndpointTreeView() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(Composite parent) {

		shell = parent.getShell();

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

		manager.add(addServerAction);
		manager.add(editServerAction);

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

	}

}
