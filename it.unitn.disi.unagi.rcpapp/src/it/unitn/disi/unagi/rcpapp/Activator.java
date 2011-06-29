package it.unitn.disi.unagi.rcpapp;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle.
 * 
 * @author Vítor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class Activator extends AbstractUIPlugin {
	/** The plug-in ID. */
	public static final String PLUGIN_ID = "it.unitn.disi.unagi.rcpapp"; //$NON-NLS-1$

	/** The shared instance. */
	private static Activator plugin;

	/** Default constructor. */
	public Activator() {}

	/** @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext) */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/** @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext) */
	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance.
	 * 
	 * @return The shared instance.
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in relative path.
	 * 
	 * @param path
	 *          The path.
	 * @return The image descriptor.
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
}
