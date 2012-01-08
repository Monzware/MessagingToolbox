package com.monzware.messaging.ui.preferences.jboss;

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.monzware.messaging.toolbox.jboss.Activator;

public class VendorPreferences extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public VendorPreferences() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Configures the JBoss 5.1.0 message provider");
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */
	public void createFieldEditors() {
		addField(new DirectoryFieldEditor(VendorPreferenceConstants.P_PATH, "&JBoss 5.1.0 path:", getFieldEditorParent()));
		addField(new StringFieldEditor(VendorPreferenceConstants.DEFAULTSERVER, "&Default server:", getFieldEditorParent()));
		addField(new StringFieldEditor(VendorPreferenceConstants.DEFAULTPORT, "&Default port:", getFieldEditorParent()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}

}