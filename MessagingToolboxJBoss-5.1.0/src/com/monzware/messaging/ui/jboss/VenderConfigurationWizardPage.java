package com.monzware.messaging.ui.jboss;

import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.monzware.messaging.toolbox.jboss.Activator;
import com.monzware.messaging.ui.preferences.jboss.VendorPreferenceConstants;

public class VenderConfigurationWizardPage extends WizardPage {

	private Text path;

	private Composite parent;

	public VenderConfigurationWizardPage() {
		super("JBoss configuration");
	}

	@Override
	public void createControl(Composite parent) {
		this.parent = parent;

		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 2;
		layout.verticalSpacing = 9;

		Label label = new Label(container, SWT.NULL);
		label.setText("&Path to JBoss:");

		IPreferenceStore ps = Activator.getDefault().getPreferenceStore();
		String configPath = ps.getString(VendorPreferenceConstants.P_PATH);

		path = new Text(container, SWT.BORDER | SWT.SINGLE);
		path.setEditable(false);
		path.setText(configPath);

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		path.setLayoutData(gd);

		Button button = new Button(container, SWT.NULL);
		button.setText("Config");

		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				openPreferences();
			}

		});

		setControl(container);

	}

	private void openPreferences() {

		Shell shell = parent.getShell();
		
		PreferenceManager mgr = new PreferenceManager();

		// IPreferenceNode node = new PreferenceNode("1", page);
		IPreferenceNode node = new VendorPreferenceNode("1");

		mgr.addToRoot(node);
		PreferenceDialog dialog = new PreferenceDialog(shell, mgr);
		dialog.create();
		dialog.setMessage("JBoss 5.1.0 Configuration");
		dialog.open();

		IPreferenceStore ps = Activator.getDefault().getPreferenceStore();
		String configPath = ps.getString(VendorPreferenceConstants.P_PATH);
		path.setText(configPath);
	}
}
