package it.unitn.disi.unagi.application;

import org.eclipse.core.runtime.Plugin;

/**
 * The activator class controls the plug-in life cycle.
 * 
 * @author Vítor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class Activator extends Plugin {
	/** The plug-in ID. */
	public static final String PLUGIN_ID = "it.unitn.disi.unagi.application"; //$NON-NLS-1$

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
}
