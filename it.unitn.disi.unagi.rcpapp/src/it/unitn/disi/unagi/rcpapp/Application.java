package it.unitn.disi.unagi.rcpapp;

import it.unitn.disi.unagi.application.exceptions.UnagiException;
import it.unitn.disi.unagi.application.services.Unagi;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.Status;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

/**
 * The main program. This class controls all aspects of the application's execution.
 * 
 * @author Vítor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class Application implements IApplication {
	/** The plug-in logger. */
	private static final ILog log = Activator.getInstance().getLog();
	
	/** @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.IApplicationContext) */
	@Override
	public Object start(IApplicationContext context) throws Exception {
		log.log(new Status(Status.INFO, Activator.PLUGIN_ID, "Unagi starting...")); //$NON-NLS-1$
		
		// Creates the SWT display.
		Display display = PlatformUI.createDisplay();
		
		// Initializes the Unagi application.
		try {
			Unagi.initialize();
		}
		catch (UnagiException e) {
			// Logs and displays an error message.
			Status status = new Status(Status.ERROR, Activator.PLUGIN_ID, "Failed to initialize the Unagi application. Please contact technical support.", e); //$NON-NLS-1$
			log.log(status);
			ErrorDialog.openError(display.getActiveShell(), "Fatal error", "The application could not be started", status); //$NON-NLS-1$ //$NON-NLS-2$
			
			// Exits the application.
			return IApplication.EXIT_OK;
		}

		// Creates the workbench and passes control to it, waiting for it to return when the user exits the application.
		try {
			int returnCode = PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor());
			if (returnCode == PlatformUI.RETURN_RESTART)
				return IApplication.EXIT_RESTART;
			else return IApplication.EXIT_OK;
		}
		finally {
			display.dispose();
		}
	}

	/** @see org.eclipse.equinox.app.IApplication#stop() */
	@Override
	public void stop() {
		log.log(new Status(Status.INFO, Activator.PLUGIN_ID, "Unagi stopping...")); //$NON-NLS-1$

		if (!PlatformUI.isWorkbenchRunning())
			return;
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final Display display = workbench.getDisplay();
		display.syncExec(new Runnable() {
			public void run() {
				if (!display.isDisposed())
					workbench.close();
			}
		});
	}
}
