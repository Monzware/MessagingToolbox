package com.monzware.messaging.toolbox.jboss510.classloader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.preference.IPreferenceStore;

import com.monzware.messaging.toolbox.jboss.Activator;
import com.monzware.messaging.ui.preferences.jboss.VendorPreferenceConstants;

public class JBossClientClassLoaderManager {

	private static Map<String, URLClassLoader> classloaders = new HashMap<String, URLClassLoader>();

	public static ClassLoader getClassLoader(ClassLoader parent) throws MalformedURLException {
		IPreferenceStore ps = Activator.getDefault().getPreferenceStore();
		String configPath = ps.getString(VendorPreferenceConstants.P_PATH);

		String path = configPath + File.separator + "client" + File.separator + "jbossall-client.jar";

		URLClassLoader urlClassLoader = classloaders.get(path);
		if (urlClassLoader == null) {
			File file = new File(path);
			URL[] urls = new URL[1];
			urls[0] = file.toURI().toURL();
			urlClassLoader = new URLClassLoader(urls, parent);
			classloaders.put(path, urlClassLoader);
		}

		return urlClassLoader;
	}
}
