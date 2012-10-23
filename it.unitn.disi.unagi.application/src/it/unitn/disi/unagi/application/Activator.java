package it.unitn.disi.unagi.application;

import it.unitn.disi.util.logging.LogUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.ServiceTracker;

/**
 * The Application bundle's activator. An instance of this class is loaded when the bundle is activated and contains
 * global bundle information.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class Activator implements BundleActivator {
	/** Path to the bundle's properties file. */
	private static final String PROPERTIES_PATH = "/META-INF/unagi-application.properties"; //$NON-NLS-1$

	/** TODO: document this field. */
	public static final String PROPERTY_COMPILER_TEMPLATE_FILE = "compiler.templateFile"; //$NON-NLS-1$

	/** The bundle's context. */
	private static BundleContext context;

	/** The bundle itself. */
	private static Bundle bundle;

	/** The bundle's ID. */
	private static String bundleId;

	/** Properties of this bundle, used by other classes. */
	private static Properties properties;

	/** Getter for bundle. */
	public static Bundle getBundle() {
		return bundle;
	}

	/** Getter for bundleId. */
	public static String getBundleId() {
		return bundleId;
	}

	/** Getter for context. */
	public static BundleContext getContext() {
		return context;
	}

	/** @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext) */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		Activator.bundle = context.getBundle();
		Activator.bundleId = bundle.getSymbolicName();

		// Initializes the logger.
		ServiceTracker<LogService, LogService> logTracker = new ServiceTracker<LogService, LogService>(context, LogService.class, null);
		logTracker.open();
		LogUtil.initialize(logTracker.getService());
		LogUtil.log.info("Unagi Application Module has started."); //$NON-NLS-1$

		// Loads the bundle's properties.
		loadProperties();
	}

	/** @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext) */
	public void stop(BundleContext bundleContext) throws Exception {
		LogUtil.log.info("Unagi Application Module has stopped."); //$NON-NLS-1$
	}
	
	/**
	 * Internal method that loads the bundle's properties into memory, making them available for other classes in the
	 * bundle through the getProperty() method.
	 * 
	 * @throws IOException
	 *           If there are any I/O problems while reading the properties from the file.
	 */
	private static void loadProperties() throws IOException {
		// Loads the properties file from the bundle.
		Bundle bundle = Activator.getContext().getBundle();
		URL propsUrl = bundle.getEntry(PROPERTIES_PATH);
		InputStream in = propsUrl.openStream();

		// Creates a properties object with its contents.
		properties = new Properties();
		properties.load(in);
		in.close();
	}

	/**
	 * Gets the value of a bundle property, given its key.
	 * 
	 * @param key
	 *          The key of the desired property.
	 * @return The value of the property whose key was specified, if it exists in the file.
	 */
	public static String getProperty(String key) {
		return properties.getProperty(key);
	}
}
