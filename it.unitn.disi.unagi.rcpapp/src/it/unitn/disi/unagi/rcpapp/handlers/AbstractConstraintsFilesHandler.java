package it.unitn.disi.unagi.rcpapp.handlers;

import it.unitn.disi.unagi.application.exceptions.UnagiException;
import it.unitn.disi.unagi.rcpapp.views.UnagiProjectExplorerView;
import it.unitn.disi.unagi.rcpapp.views.models.ConstraintsFileProjectTreeElement;
import it.unitn.disi.util.gui.DialogUtil;
import it.unitn.disi.util.logging.LogUtil;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;

/**
 * Abstract class for command handlers in the RCP App bundle that deal with one or more constraints file in the
 * execution of the command. Contains methods that check and manipulate the selected constraints files in the workspace,
 * delegating the specific execution to the concrete subclass.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public abstract class AbstractConstraintsFilesHandler extends AbstractHandler {
	/**
	 * Verifies if a single constraints file is selected in the workspace. Should be used by handlers whose commands
	 * operate in one and only one constraints file.
	 * 
	 * @param selectionService
	 *          The platform's selection service, used to determine what is the user's current workspace selection.
	 * @return <code>true</code> if there's exactly one constraints file selected, <code>false</code> otherwise.
	 */
	public boolean isSingleConstraintsSelected(ESelectionService selectionService) {
		Object selection = selectionService.getSelection(UnagiProjectExplorerView.VIEW_ID);
		return ((selection != null) && (selection instanceof ConstraintsFileProjectTreeElement));
	}

	/**
	 * Verifies if at least one constraints file is selected in the workspace. Should be used by handlers whose commands
	 * operate in one or more constraints file at once.
	 * 
	 * @param selectionService
	 *          The platform's selection service, used to determine what is the user's current workspace selection.
	 * @return <code>true</code> if at least one constraints file is selected, <code>false</code> otherwise.
	 */
	public boolean isAtLeastOneConstraintsSelected(ESelectionService selectionService) {
		Object selection = selectionService.getSelection(UnagiProjectExplorerView.VIEW_ID);

		// Single selection of a project tree element is OK.
		if ((selection != null) && (selection instanceof ConstraintsFileProjectTreeElement))
			return true;

		// Otherwise it has to be an array (multiple selection).
		if (!(selection instanceof Object[]))
			return false;

		// Checks if the array contains only constraints elements.
		Object[] multipleSelection = (Object[]) selection;
		for (Object obj : multipleSelection)
			if (!(obj instanceof ConstraintsFileProjectTreeElement))
				return false;

		// If it was an array and all elements were constraints§, then it's OK.
		return true;
	}

	/**
	 * Retrieves the single selected constraints file from the workspace.
	 * 
	 * Note that this method does not check if only a single constraints file was selected, returning null if that is not
	 * the case.
	 * 
	 * @param selectionService
	 *          The platform's selection service, used to determine what is the user's current workspace selection.
	 * @return The single-selected constraints file form the workspace, or <code>null</code> if a single constraints file
	 *         was not selected.
	 */
	public IFile retrieveSingleSelectedConstraints(ESelectionService selectionService) {
		// First checks that there is a single constraints element selected.
		Object selection = selectionService.getSelection(UnagiProjectExplorerView.VIEW_ID);
		if ((selection == null) || (!(selection instanceof ConstraintsFileProjectTreeElement)))
			return null;

		// Then retrieves the file to which the selected element refers.
		ConstraintsFileProjectTreeElement element = (ConstraintsFileProjectTreeElement) selection;
		return element.getConstraintsFile();
	}

	/**
	 * Retrieves all selected constraints files from the workspace.
	 * 
	 * Note that this method does not check if at least one constraints file was selected, returning an empty set if that
	 * is not the case.
	 * 
	 * @param selectionService
	 *          The platform's selection service, used to determine what is the user's current workspace selection.
	 * @return A set containing all constraints files that are currently selected in the workspace.
	 */
	public Set<IFile> retrieveMultipleSelectedConstraintss(ESelectionService selectionService) {
		Set<IFile> constraints = new HashSet<>();
		Object selection = selectionService.getSelection(UnagiProjectExplorerView.VIEW_ID);

		// First checks if single selection of constraints, returning a set with a single element in this case.
		if ((selection != null) && (selection instanceof ConstraintsFileProjectTreeElement)) {
			IFile model = ((ConstraintsFileProjectTreeElement) selection).getConstraintsFile();
			constraints.add(model);
		}

		// Otherwise it has to be an array (multiple selection). Add all constraints (single and package).
		else if (selection instanceof Object[]) {
			Object[] multipleSelection = (Object[]) selection;
			for (Object obj : multipleSelection) {
				if (obj instanceof ConstraintsFileProjectTreeElement) {
					IFile model = ((ConstraintsFileProjectTreeElement) obj).getConstraintsFile();
					constraints.add(model);
				}
			}
		}

		return constraints;
	}

	/**
	 * Executes the command for a single-selected constraints file.
	 * 
	 * Note that this method does not check if only a single constraints file was selected, doing nothing if that is not
	 * the case.
	 * 
	 * @param selectionService
	 *          The platform's selection service, used to determine what is the user's current workspace selection.
	 */
	public void executeForSingleConstraints(ESelectionService selectionService) {
		IFile constraints = retrieveSingleSelectedConstraints(selectionService);
		if (constraints != null)
			handleSingleConstraints(constraints);
	}

	/**
	 * Executes the command for all selected constraints files.
	 * 
	 * Note that this method does not check if at least one constraints file was selected, doing nothing if that is not
	 * the case.
	 * 
	 * @param selectionService
	 *          The platform's selection service, used to determine what is the user's current workspace selection.
	 */
	public void executeForMultipleConstraintss(ESelectionService selectionService) {
		Set<IFile> constraints = retrieveMultipleSelectedConstraintss(selectionService);
		for (IFile model : constraints) {
			handleSingleConstraints(model);
		}
	}

	/**
	 * Internal template method that is called by the execute methods above, which performs the generic part of command
	 * handling and delegates the specific part of the command to the concrete subclass via the doExecute() method.
	 * 
	 * @param constraints
	 *          The constraints file to handle.
	 */
	protected void handleSingleConstraints(final IFile constraints) {
		final String name = constraints.getName();
		final String cmdName = getClass().getName();
		LogUtil.log.debug("Executing \"{0}\" command for constraints file: {1}", cmdName, name); //$NON-NLS-1$

		// Creates and schedules the job that will execute the command.
		Job job = new Job(getJobDescription(name)) {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					// Delegates the execution to the extending class.
					doExecute(monitor, constraints);
				}
				catch (UnagiException e) {
					LogUtil.log.error("The \"{0}\" command was unable to be performed for constraints file: {1}.", e, cmdName, name); //$NON-NLS-1$

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
	 * Abstract method called by the template method handleSingleConstraints() so the specific part of command handling
	 * can be delegated to the concrete subclass.
	 * 
	 * @param monitor
	 *          The workbench's progress monitor, in case it's a long-running task.
	 * @param constraints
	 *          The constraints file to handle.
	 * @throws UnagiException
	 *           If anything goes wrong during the specific handling of the constraints file.
	 */
	protected abstract void doExecute(IProgressMonitor monitor, IFile constraints) throws UnagiException;
}
