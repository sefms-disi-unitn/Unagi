package it.unitn.disi.unagi.gui;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {
	/** The plug-in ID. */
	public static final String PLUGIN_ID = "it.unitn.disi.unagi.gui"; //$NON-NLS-1$

	/** The shared instance of the plug-in, used to access plug-in features such as logging. */
	private static Activator plugin;
	
	/** Default constructor. */
	public Activator() {
	}

	/** @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext) */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/** @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext) */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Provides the shared instance of the plug-in for other classes within the bundle.
	 * 
	 * @return The shared instance of the plug-in.
	 */
	public static Activator getDefault() {
		return plugin;
	}

}
