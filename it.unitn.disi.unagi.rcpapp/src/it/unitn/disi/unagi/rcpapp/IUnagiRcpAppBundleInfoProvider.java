package it.unitn.disi.unagi.rcpapp;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * Interface for an application-level service that profiles global information on the RCP Application bundle. This
 * interface is to be implemented by the bundle's Activator and be registered as an OSGi component.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public interface IUnagiRcpAppBundleInfoProvider {
	/**
	 * Retrieves the bundle object that represents the RCP App bundle.
	 * 
	 * @return The bundle object that represents the RCP App bundle.
	 */
	Bundle getBundle();

	/**
	 * Retrieves the bundle's ID.
	 * 
	 * @return A string containing the bundle's ID.
	 */
	String getBundleId();

	/**
	 * Retrieves the bundle's context.
	 * 
	 * @return The bundle's context.
	 */
	BundleContext getContext();

	/**
	 * Retrieves the ID of the part stack in which editors should be opened.
	 * 
	 * @return The ID of the part stack in which editors should be opened.
	 */
	String getEditorStackId();
}
