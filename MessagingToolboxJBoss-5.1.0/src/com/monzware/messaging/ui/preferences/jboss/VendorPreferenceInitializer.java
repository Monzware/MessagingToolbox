package com.monzware.messaging.ui.preferences.jboss;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.monzware.messaging.toolbox.jboss.Activator;

/**
 * Class used to initialize default preference values.
 */
public class VendorPreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#
	 * initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		//IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		/*store.setDefault(VendorPreferenceConstants.P_BOOLEAN, true);
		store.setDefault(VendorPreferenceConstants.P_CHOICE, "choice2");
		store.setDefault(VendorPreferenceConstants.P_STRING, "Default value");*/
	}

}
