package it.unitn.disi.unagi.rcpapp.handlers;

import it.unitn.disi.unagi.application.exceptions.UnagiException;
import it.unitn.disi.unagi.application.services.IManageProjectsService;
import it.unitn.disi.util.logging.LogUtil;

import javax.inject.Inject;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;

/**
 * Handler for the "Close Projects" command.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class CloseProjectsHandler extends AbstractProjectsHandler {
	/** Service class for project management. */
	@Inject
	private IManageProjectsService manageProjectsService;

	/**
	 * Method that handles the command's execution.
	 * 
	 * @param selectionService
	 *          The platform's selection service, used to determine what is the user's current workspace selection.
	 */
	@Execute
	public void execute(ESelectionService selectionService) {
		LogUtil.log.debug("Executing \"Close Project(s)\" command."); //$NON-NLS-1$

		// This command can be executed for multiple projects.
		executeForMultipleProjects(selectionService);
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
		// Can only close a project if one or more elements of the project tree in the project explorer is selected.
		return isSomethingSelected(selectionService);
	}

	/**
	 * @see it.unitn.disi.unagi.rcpapp.handlers.AbstractProjectsHandler#doExecute(org.eclipse.core.runtime.IProgressMonitor,
	 *      org.eclipse.core.resources.IProject)
	 */
	@Override
	protected void doExecute(IProgressMonitor monitor, IProject project) throws UnagiException {
		// Closes the project.
		manageProjectsService.closeProject(monitor, project);
	}
}
