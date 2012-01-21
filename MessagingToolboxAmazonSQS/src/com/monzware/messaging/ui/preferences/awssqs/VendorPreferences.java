package com.monzware.messaging.ui.preferences.awssqs;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.monzware.messaging.toolbox.awssqs.Activator;

public class VendorPreferences extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public VendorPreferences() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Configures the Amazon SQS message provider");
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */
	public void createFieldEditors() {
		addField(new StringFieldEditor(VendorPreferenceConstants.ACCESKEYID, "&Access Key ID:", getFieldEditorParent()));
		addField(new StringFieldEditor(VendorPreferenceConstants.SECRETACCESSKEY, "&Secret Access Key:", getFieldEditorParent()));
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