package it.unitn.disi.unagi.rcpapp;

import it.unitn.disi.util.logging.LogUtil;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.ServiceTracker;

/**
 * The RCP App bundle's activator. An instance of this class is loaded when the bundle is activated and contains global
 * bundle information. Such information is made accessible to the rest of the bundle via the bundle info provider
 * interface.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class Activator implements BundleActivator, IUnagiRcpAppBundleInfoProvider {
	/** ID of the part stack in which editors should be opened. */
	private static final String EDITOR_STACK_ID = "it.unitn.disi.unagi.rcpapp.partstack.right"; //$NON-NLS-1$

	/** The bundle's context. */
	private static BundleContext context;

	/** The bundle itself. */
	private static Bundle bundle;

	/** The bundle's ID. */
	private static String bundleId;

	/** @see it.unitn.disi.unagi.rcpapp.IUnagiRcpAppBundleInfoProvider#getBundle() */
	public Bundle getBundle() {
		return bundle;
	}

	/** @see it.unitn.disi.unagi.rcpapp.IUnagiRcpAppBundleInfoProvider#getBundleId() */
	public String getBundleId() {
		return bundleId;
	}

	/** @see it.unitn.disi.unagi.rcpapp.IUnagiRcpAppBundleInfoProvider#getContext() */
	public BundleContext getContext() {
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
		LogUtil.log.info("Unagi RCP Application has started."); //$NON-NLS-1$
	}

	/** @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext) */
	public void stop(BundleContext bundleContext) throws Exception {
		LogUtil.log.info("Unagi RCP Application has stopped."); //$NON-NLS-1$
	}

	/** @see it.unitn.disi.unagi.rcpapp.IUnagiRcpAppBundleInfoProvider#getEditorStackId() */
	@Override
	public String getEditorStackId() {
		return EDITOR_STACK_ID;
	}
}
