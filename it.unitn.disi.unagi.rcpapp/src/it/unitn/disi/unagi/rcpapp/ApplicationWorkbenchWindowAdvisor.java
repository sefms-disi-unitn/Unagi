package it.unitn.disi.unagi.rcpapp;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

/**
 * Component responsible for the configuration of the workbench window, created by the workbench.
 * 
 * @author Vítor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {
	/** The plug-in logger. */
	private static final ILog log = Activator.getInstance().getLog();

	/** Default constructor. */
	public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	/** @see org.eclipse.ui.application.WorkbenchWindowAdvisor#createActionBarAdvisor(org.eclipse.ui.application.IActionBarConfigurer) */
	@Override
	public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
		return new ApplicationActionBarAdvisor(configurer);
	}

	/** @see org.eclipse.ui.application.WorkbenchWindowAdvisor#preWindowOpen() */
	@Override
	public void preWindowOpen() {
		log.log(new Status(Status.INFO, Activator.PLUGIN_ID, "Configuring the workbench (title, window size, etc.)...")); //$NON-NLS-1$

		// Sets the window's title and size and configures it to show both the toolbar and the status bar.
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.setTitle("Unagi"); //$NON-NLS-1$
		configurer.setInitialSize(new Point(896, 768));
		configurer.setShowCoolBar(true);
		configurer.setShowStatusLine(true);
	}

	/** @see org.eclipse.ui.application.WorkbenchWindowAdvisor#postWindowOpen() */
	@Override
	public void postWindowOpen() {
		// Closes all editors when the application is just started.
		IWorkbenchPage page = getWindowConfigurer().getWindow().getActivePage();
		page.closeAllEditors(true);
	}
}
