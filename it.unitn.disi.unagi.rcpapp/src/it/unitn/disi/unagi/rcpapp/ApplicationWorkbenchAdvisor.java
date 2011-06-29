package it.unitn.disi.unagi.rcpapp;

import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

/**
 * Component responsible for the configuration of the workbench. 
 * 
 * @author Vítor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {
	/** The perspective ID. */
	private static final String PERSPECTIVE_ID = "it.unitn.disi.unagi.rcpapp.perspective"; //$NON-NLS-1$

	/** @see org.eclipse.ui.application.WorkbenchAdvisor#createWorkbenchWindowAdvisor(org.eclipse.ui.application.IWorkbenchWindowConfigurer) */
	@Override
	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		return new ApplicationWorkbenchWindowAdvisor(configurer);
	}

	/** @see org.eclipse.ui.application.WorkbenchAdvisor#getInitialWindowPerspectiveId() */
	@Override
	public String getInitialWindowPerspectiveId() {
		return PERSPECTIVE_ID;
	}

	/** @see org.eclipse.ui.application.WorkbenchAdvisor#initialize(org.eclipse.ui.application.IWorkbenchConfigurer) */
	@Override
	public void initialize(IWorkbenchConfigurer configurer) {
		super.initialize(configurer);
		
		// Configures the workbench to remember the user's layout and window size the next time the application is started.
		configurer.setSaveAndRestore(true);
	}
}
