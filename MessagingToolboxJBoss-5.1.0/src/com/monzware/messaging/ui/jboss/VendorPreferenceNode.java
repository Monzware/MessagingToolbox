package com.monzware.messaging.ui.jboss;

import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.PreferenceNode;

import com.monzware.messaging.ui.preferences.jboss.VendorPreferences;

public class VendorPreferenceNode extends PreferenceNode implements IPreferenceNode {

	public VendorPreferenceNode(String id) {
		super(id, new VendorPreferences());
	}

	@Override
	public String getLabelText() {
		return "JBoss 5.1.0 Configuration";
	}
}
