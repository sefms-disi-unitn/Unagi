package it.unitn.disi.unagi.rcpapp.handlers;

import it.unitn.disi.unagi.rcpapp.wizards.OpenProjectsWizard;
import it.unitn.disi.util.logging.LogUtil;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

/**
 * Handler for the "Open Projects" command.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class OpenProjectsHandler extends AbstractHandler {
	/**
	 * Method that handles the command's execution.
	 * 
	 * @param activeShell
	 *          The SWT Shell that is currently active.
	 * @param wizard
	 *          An Open Projects wizard that will be automatically created and injected by the DI mechanism.
	 */
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell activeShell, OpenProjectsWizard wizard) {
		LogUtil.log.debug("Executing \"Open Project(s)\" command."); //$NON-NLS-1$

		// Opens the "Open Project(s)" wizard that was created and injected automatically by Eclipse.
		WizardDialog dialog = new WizardDialog(activeShell, wizard);
		dialog.open();
	}
}
