package it.unitn.disi.unagi.rcpapp.handlers;

import it.unitn.disi.util.logging.LogUtil;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.workbench.IWorkbench;

/**
 * Handler for the "Quit" command.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class QuitHandler extends AbstractHandler {
	/**
	 * Method that handles the command's execution.
	 * 
	 * @param workbench
	 *          Reference to the Eclipse workbench, injected automatically by the DI framework.
	 */
	@Execute
	public void execute(IWorkbench workbench) {
		LogUtil.log.debug("Executing \"Quit\" command."); //$NON-NLS-1$

		// Closes the workbench, effectively quitting the application.
		workbench.close();
	}
}
