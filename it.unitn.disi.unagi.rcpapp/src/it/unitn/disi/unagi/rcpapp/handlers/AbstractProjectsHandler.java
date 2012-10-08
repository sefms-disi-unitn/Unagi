package it.unitn.disi.unagi.rcpapp.handlers;

import it.unitn.disi.unagi.application.exceptions.UnagiException;
import it.unitn.disi.unagi.rcpapp.views.UnagiProjectExplorerView;
import it.unitn.disi.unagi.rcpapp.views.models.AbstractProjectTreeElement;
import it.unitn.disi.util.gui.DialogUtil;
import it.unitn.disi.util.logging.LogUtil;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;

/**
 * Abstract class for command handlers in the RCP App bundle that deal with one or more projects in the execution of the
 * command. Contains methods that check and manipulate the selected projects in the workspace, delegating the specific
 * execution to the concrete subclass.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public abstract class AbstractProjectsHandler extends AbstractHandler {
	/**
	 * Verifies if a single project is selected in the workspace. Should be used by handlers whose commands operate in one
	 * and only one project.
	 * 
	 * Note that if any element of the project tree is selected, that project is considered selected.
	 * 
	 * @param selectionService
	 *          The platform's selection service, used to determine what is the user's current workspace selection.
	 * @return <code>true</code> if there's exactly one project selected, <code>false</code> otherwise.
	 */
	public boolean isSingleProjectSelected(ESelectionService selectionService) {
		Object selection = selectionService.getSelection(UnagiProjectExplorerView.VIEW_ID);
		return ((selection != null) && (selection instanceof AbstractProjectTreeElement));
	}

	/**
	 * Verifies if at least one project is selected in the workspace. Should be used by handlers whose commands operate in
	 * one or more projects at once.
	 * 
	 * Note that if any element of the project tree is selected, that project is considered selected.
	 * 
	 * @param selectionService
	 *          The platform's selection service, used to determine what is the user's current workspace selection.
	 * @return <code>true</code> if at least one project is selected, <code>false</code> otherwise.
	 */
	public boolean isSomethingSelected(ESelectionService selectionService) {
		Object selection = selectionService.getSelection(UnagiProjectExplorerView.VIEW_ID);

		// Single selection of a project tree element is OK.
		if ((selection != null) && (selection instanceof AbstractProjectTreeElement))
			return true;

		// Otherwise it has to be an array (multiple selection).
		if (!(selection instanceof Object[]))
			return false;

		// Checks if the array contains only project tree elements.
		Object[] multipleSelection = (Object[]) selection;
		for (Object obj : multipleSelection)
			if (!(obj instanceof AbstractProjectTreeElement))
				return false;

		// If it was an array and all elements were project tree elements, then it's OK.
		return true;
	}

	/**
	 * Retrieves the single selected project from the workspace.
	 * 
	 * Note that if any element of the project tree is selected, that element's project is considered selected.
	 * Furthermore, this method does not check if only a single project was selected, returning null if that is not the
	 * case.
	 * 
	 * @param selectionService
	 *          The platform's selection service, used to determine what is the user's current workspace selection.
	 * @return The single-selected project form the workspace, or <code>null</code> if a single element was not selected.
	 */
	public IProject retrieveSingleSelectedProject(ESelectionService selectionService) {
		// First checks that there is a single element selected.
		Object selection = selectionService.getSelection(UnagiProjectExplorerView.VIEW_ID);
		if ((selection == null) || (!(selection instanceof AbstractProjectTreeElement)))
			return null;

		// Then retrieves the project to which the selected element belongs.
		AbstractProjectTreeElement element = (AbstractProjectTreeElement) selection;
		return element.getProject();
	}

	/**
	 * Retrieves all selected project from the workspace.
	 * 
	 * Note that if any element of the project tree is selected, that element's project is considered selected.
	 * Furthermore, this method does not check if at least one project was selected, returning an empty set if that is not
	 * the case.
	 * 
	 * @param selectionService
	 *          The platform's selection service, used to determine what is the user's current workspace selection.
	 * @return A set containing all projects that are currently selected in the workspace.
	 */
	public Set<IProject> retrieveMultipleSelectedProjects(ESelectionService selectionService) {
		Set<IProject> projects = new HashSet<>();
		Object selection = selectionService.getSelection(UnagiProjectExplorerView.VIEW_ID);

		// First checks if single selection, returning a set with a single element in this case.
		if ((selection != null) && (selection instanceof AbstractProjectTreeElement)) {
			IProject project = ((AbstractProjectTreeElement) selection).getProject();
			projects.add(project);
		}

		// Otherwise it has to be an array (multiple selection). Add all projects.
		else if (selection instanceof Object[]) {
			Object[] multipleSelection = (Object[]) selection;
			for (Object obj : multipleSelection) {
				if (obj instanceof AbstractProjectTreeElement) {
					IProject project = ((AbstractProjectTreeElement) obj).getProject();
					projects.add(project);
				}
			}
		}

		return projects;
	}

	/**
	 * Executes the command for a single-selected project.
	 * 
	 * Note that if any element of the project tree is selected, that element's project is considered selected.
	 * Furthermore, this method does not check if only a single project was selected, doing nothing if that is not the
	 * case.
	 * 
	 * @param selectionService
	 *          The platform's selection service, used to determine what is the user's current workspace selection.
	 */
	public void executeForSingleProject(ESelectionService selectionService) {
		IProject project = retrieveSingleSelectedProject(selectionService);
		if (project != null)
			handleSingleProject(project);
	}

	/**
	 * Executes the command for all selected projects.
	 * 
	 * Note that if any element of the project tree is selected, that element's project is considered selected.
	 * Furthermore, this method does not check if at least one project was selected, doing nothing if that is not the
	 * case.
	 * 
	 * @param selectionService
	 *          The platform's selection service, used to determine what is the user's current workspace selection.
	 */
	public void executeForMultipleProjects(ESelectionService selectionService) {
		Set<IProject> projects = retrieveMultipleSelectedProjects(selectionService);
		for (IProject project : projects) {
			handleSingleProject(project);
		}
	}

	/**
	 * Internal template method that is called by the execute methods above, which performs the generic part of command
	 * handling and delegates the specific part of the command to the concrete subclass via the doExecute() method.
	 * 
	 * @param project
	 *          The project to handle.
	 */
	protected void handleSingleProject(final IProject project) {
		final String name = project.getName();
		final String cmdName = getClass().getName();
		LogUtil.log.debug("Executing \"{0}\" command for project: {1}", cmdName, name); //$NON-NLS-1$

		// Creates and schedules the job that will execute the command.
		Job job = new Job(getJobDescription(name)) {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					// Delegates the execution to the extending class.
					doExecute(monitor, project);
				}
				catch (UnagiException e) {
					LogUtil.log.error("The \"{0}\" command was unable to be performed for project: {1}.", e, cmdName, name); //$NON-NLS-1$

					// If a problem occurred, shows an error message.
					final String statusMsg = getJobErrorStatus(name);
					final String errorTitle = getJobErrorTitle(name);
					final String errorMessage = getJobErrorMessage(name);
					return DialogUtil.displayError(activator.getBundleId(), statusMsg, errorTitle, errorMessage);
				}

				return Status.OK_STATUS;
			}
		};
		job.setUser(true);
		job.schedule();
	}

	/**
	 * Abstract method called by the template method handleSingleProject() so the specific part of command handling can be
	 * delegated to the concrete subclass.
	 * 
	 * @param monitor
	 *          The workbench's progress monitor, in case it's a long-running task.
	 * @param project
	 *          The project to handle.
	 * @throws UnagiException
	 *           If anything goes wrong during the specific handling of the project.
	 */
	protected abstract void doExecute(IProgressMonitor monitor, IProject project) throws UnagiException;
}
