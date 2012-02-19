package com.monzware.messaging.ui.awssqs;

import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.PreferenceNode;

import com.monzware.messaging.ui.preferences.awssqs.VendorPreferences;

public class VendorPreferenceNode extends PreferenceNode implements IPreferenceNode {

	public VendorPreferenceNode(String id) {
		super(id, new VendorPreferences());
	}

	public String getLabelText() {
		return "JBoss 5.1.0 Configuration";
	}
}
