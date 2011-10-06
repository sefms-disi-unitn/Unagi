package it.unitn.disi.unagi.rcpapp;

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
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();

		// Sets the window's title and size and configures it to show both the toolbar and the status bar.
		configurer.setTitle("Unagi");
		configurer.setInitialSize(new Point(896, 768));
		configurer.setShowCoolBar(true);
		configurer.setShowStatusLine(true);
	}

	/** @see org.eclipse.ui.application.WorkbenchWindowAdvisor#preWindowShellClose() */
	@Override
	public boolean preWindowShellClose() {
		// Closes all editors before closing the program so they will not be opened the next time the program starts.
		IWorkbenchPage page = getWindowConfigurer().getWindow().getActivePage();
		page.closeAllEditors(true);
		return true;
	}
}
