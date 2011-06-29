package it.unitn.disi.unagi.rcpapp;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

/**
 * Component responsible for the configuration of the action bars of a workbench window.
 * 
 * @author Vítor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor {
	/** Default constructor. */
	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
	}

	/** @see org.eclipse.ui.application.ActionBarAdvisor#makeActions(org.eclipse.ui.IWorkbenchWindow) */
	@Override
	protected void makeActions(IWorkbenchWindow window) {}

	/** @see org.eclipse.ui.application.ActionBarAdvisor#fillMenuBar(org.eclipse.jface.action.IMenuManager) */
	@Override
	protected void fillMenuBar(IMenuManager menuBar) {}

}
