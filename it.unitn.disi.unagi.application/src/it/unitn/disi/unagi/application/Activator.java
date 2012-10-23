package it.unitn.disi.unagi.application;

import it.unitn.disi.util.logging.LogUtil;

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
	/** The bundle's context. */
	private static BundleContext context;

	/** The bundle itself. */
	private static Bundle bundle;

	/** The bundle's ID. */
	private static String bundleId;

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
	}

	/** @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext) */
	public void stop(BundleContext bundleContext) throws Exception {
		LogUtil.log.info("Unagi Application Module has stopped."); //$NON-NLS-1$
	}
}
