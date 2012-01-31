package com.monzware.messaging.toolbox;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.menus.AbstractContributionFactory;
import org.eclipse.ui.menus.IMenuService;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.progress.IProgressService;
import org.osgi.framework.BundleContext;

import com.monzware.messaging.toolbox.menucontribution.SendToContributionFactory;

/**
 * The activator class controls the plug-in life cycle
 */
public class MessagingToolboxPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "MessagingToolbox"; //$NON-NLS-1$

	// The shared instance
	private static MessagingToolboxPlugin plugin;

	private BundleContext context;

	private EndpointManager endpointManager;

	/**
	 * The constructor
	 */
	public MessagingToolboxPlugin() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		this.context = context;
		super.start(context);
		plugin = this;

		addPopupMenuContribution();

		ImageDescriptor myImage = ImageDescriptor.createFromURL(FileLocator.find(getDefault().getBundle(), new Path("/icons/database2.png"), null));

		IProgressService progressService = PlatformUI.getWorkbench().getProgressService();
		progressService.registerIconForFamily(myImage, MessagingToolboxPlugin.PLUGIN_ID + "#JOB");
	}

	public void addPopupMenuContribution() {
		IMenuService menuService = (IMenuService) PlatformUI.getWorkbench().getService(IMenuService.class);

		AbstractContributionFactory viewMenuAddition = new SendToContributionFactory();
		menuService.addContributionFactory(viewMenuAddition);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static MessagingToolboxPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	/**
	 * Returns the working set manager
	 * 
	 * @return the working set manager
	 * @since 2.0
	 */
	public EndpointManager getEndpointManager() {
		if (endpointManager == null) {
			endpointManager = new EndpointManager(context);
			endpointManager.restoreState();
		}
		return endpointManager;
	}

	public IPath getDataLocation() {
		try {
			return getStateLocation();
		} catch (IllegalStateException e) {
			return null;
		}
	}

}
