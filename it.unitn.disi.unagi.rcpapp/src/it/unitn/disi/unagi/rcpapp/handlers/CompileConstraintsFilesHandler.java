package it.unitn.disi.unagi.rcpapp.handlers;

import it.unitn.disi.unagi.application.exceptions.UnagiException;
import it.unitn.disi.unagi.application.services.IManageModelsService;
import it.unitn.disi.util.logging.LogUtil;

import javax.inject.Inject;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;

/**
 * Handler for the "Compile Constraints Files" command.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class CompileConstraintsFilesHandler extends AbstractConstraintsFilesHandler {
	/** Service class for model management. */
	@Inject
	private IManageModelsService manageModelsService;

	/**
	 * Method that handles the command's execution.
	 * 
	 * @param selectionService
	 *          The platform's selection service, used to determine what is the user's current workspace selection.
	 */
	@Execute
	public void execute(ESelectionService selectionService) {
		LogUtil.log.debug("Executing \"Compile Constraints File(s)\" command."); //$NON-NLS-1$

		// This command can be executed for multiple files.
		executeForMultipleConstraints(selectionService);
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
		// Can only compile constraints files if at least one of them is selected.
		return isAtLeastOneConstraintsSelected(selectionService);
	}

	/**
	 * @see it.unitn.disi.unagi.rcpapp.handlers.AbstractRequirementsModelsHandler#doExecute(org.eclipse.core.runtime.IProgressMonitor,
	 *      org.eclipse.core.resources.IFile)
	 */
	@Override
	protected void doExecute(IProgressMonitor monitor, IFile file) throws UnagiException {
		// Delegates to the application class the compilation of the constraints file.
		manageModelsService.compileConstraintsFile(monitor, file);
	}
}
