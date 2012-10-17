package it.unitn.disi.unagi.rcpapp.handlers;

import it.unitn.disi.unagi.application.exceptions.UnagiException;
import it.unitn.disi.unagi.application.services.IManageConstraintsService;
import it.unitn.disi.unagi.rcpapp.nls.Messages;
import it.unitn.disi.util.logging.LogUtil;

import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

/**
 * Handler for the "Delete Constraints File" command.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class DeleteConstraintsFilesHandler extends AbstractConstraintsFilesHandler {
	/** Service class for constraints files management. */
	@Inject
	private IManageConstraintsService manageConstraintsService;

	/**
	 * Method that handles the command's execution.
	 * 
	 * @param activeShell
	 *          The SWT Shell that is currently active.
	 * @param selectionService
	 *          The platform's selection service, used to determine what is the user's current workspace selection.
	 */
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell activeShell, ESelectionService selectionService) {
		LogUtil.log.debug("Executing \"Compile Constraints File(s)\" command."); //$NON-NLS-1$

		// Confirms the deletion of the files.
		Set<IFile> files = retrieveMultipleSelectedConstraints(selectionService);
		int size = files.size();
		if (size > 0) {
			String title = Messages.getString("service." + getClassKey() + ".confirmation.title"); //$NON-NLS-1$ //$NON-NLS-2$
			String message = Messages.getFormattedString("service." + getClassKey() + ".confirmation.message", size); //$NON-NLS-1$ //$NON-NLS-2$
			boolean confirm = MessageDialog.openConfirm(activeShell, title, message);

			// This command can be executed for multiple files.
			if (confirm)
				executeForMultipleConstraints(selectionService);
		}
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
		// Can only compile requirements models if at least one of them is selected.
		return isAtLeastOneConstraintsSelected(selectionService);
	}

	/**
	 * @see it.unitn.disi.unagi.rcpapp.handlers.AbstractRequirementsModelsHandler#doExecute(org.eclipse.core.runtime.IProgressMonitor,
	 *      org.eclipse.core.resources.IFile)
	 */
	@Override
	protected void doExecute(IProgressMonitor monitor, IFile file) throws UnagiException {
		// Delegates to the application class the deletion of the given file.
		manageConstraintsService.deleteConstraintsFile(monitor, file);
	}
}
