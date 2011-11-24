package it.unitn.disi.unagi.rcpapp;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.Status;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
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
		
		Display display = PlatformUI.createDisplay();
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
