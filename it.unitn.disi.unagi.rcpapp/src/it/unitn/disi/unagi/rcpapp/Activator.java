package it.unitn.disi.unagi.rcpapp;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * The activator class controls the plug-in life cycle.
 * 
 * @author Vítor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class Activator extends AbstractUIPlugin {
	/** The plug-in ID. */
	public static final String PLUGIN_ID = "it.unitn.disi.unagi.rcpapp"; //$NON-NLS-1$

	/** The shared instance of the plug-in, used to access plug-in features such as logging. */
	private static Plugin plugin;

	/** Default constructor. */
	public Activator() {
		plugin = this;		
	}

	/**
	 * Provides the shared instance of the plug-in for other classes within the bundle.
	 * 
	 * @return The shared instance of the plug-in.
	 */
	public static Plugin getInstance() {
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
