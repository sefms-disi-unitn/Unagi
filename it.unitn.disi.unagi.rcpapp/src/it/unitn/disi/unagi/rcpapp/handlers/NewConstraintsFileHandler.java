package it.unitn.disi.unagi.rcpapp.handlers;

import it.unitn.disi.unagi.application.exceptions.UnagiException;
import it.unitn.disi.unagi.rcpapp.wizards.CreateNewConstraintsFileWizard;
import it.unitn.disi.util.logging.LogUtil;

import javax.inject.Named;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

/**
 * Handler for the "New Constraints File" command.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class NewConstraintsFileHandler extends AbstractProjectsHandler {
	/**
	 * Method that handles the command's execution.
	 * 
	 * @param activeShell
	 *          The SWT Shell that is currently active.
	 * @param wizard
	 *          A Create New Constraints File wizard that will be automatically created and injected by the DI mechanism.
	 */
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell activeShell, CreateNewConstraintsFileWizard wizard) {
		LogUtil.log.debug("Executing \"New Constraints File\" command."); //$NON-NLS-1$

		// Opens the "Create new constraints file" wizard that was created and injected automatically by Eclipse.
		WizardDialog dialog = new WizardDialog(activeShell, wizard);
		dialog.open();
	}

	/**
	 * Method that informs the workbench if the command can be executed.
	 * 
	 * @param selectionService
	 *          The platform's selection service, used to determine what is the user's current workspace selection.
	 * @return <code>true</code> if the current selection allows the execution of the command, <code>false</code>
	 *         otherwise.
	 */
	@CanExecute
	public boolean canExecute(ESelectionService selectionService) {
		// Can only create new file if a single element of the project tree in the project explorer is selected.
		return isSingleProjectSelected(selectionService);
	}

	/**
	 * @see it.unitn.disi.unagi.rcpapp.handlers.AbstractProjectsHandler#doExecute(org.eclipse.core.runtime.IProgressMonitor,
	 *      org.eclipse.core.resources.IProject)
	 */
	@Override
	protected void doExecute(IProgressMonitor monitor, IProject project) throws UnagiException {}
}
