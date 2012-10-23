package it.unitn.disi.unagi.rcpapp.handlers;

import it.unitn.disi.unagi.application.exceptions.UnagiException;
import it.unitn.disi.unagi.rcpapp.views.UnagiProjectExplorerView;
import it.unitn.disi.unagi.rcpapp.views.models.RequirementsModelProjectTreeElement;
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
 * Abstract class for command handlers in the RCP App bundle that deal with one or more models in the execution of the
 * command. Contains methods that check and manipulate the selected models in the workspace, delegating the specific
 * execution to the concrete subclass.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public abstract class AbstractRequirementsModelsHandler extends AbstractHandler {
	/**
	 * Verifies if a single model is selected in the workspace. Should be used by handlers whose commands operate in one
	 * and only one model.
	 * 
	 * @param selectionService
	 *          The platform's selection service, used to determine what is the user's current workspace selection.
	 * @return <code>true</code> if there's exactly one model selected, <code>false</code> otherwise.
	 */
	public boolean isSingleModelSelected(ESelectionService selectionService) {
		Object selection = selectionService.getSelection(UnagiProjectExplorerView.VIEW_ID);
		return ((selection != null) && (selection instanceof RequirementsModelProjectTreeElement));
	}

	/**
	 * Verifies if at least one model is selected in the workspace. Should be used by handlers whose commands operate in
	 * one or more models at once.
	 * 
	 * @param selectionService
	 *          The platform's selection service, used to determine what is the user's current workspace selection.
	 * @return <code>true</code> if at least one model is selected, <code>false</code> otherwise.
	 */
	public boolean isAtLeastOneModelSelected(ESelectionService selectionService) {
		Object selection = selectionService.getSelection(UnagiProjectExplorerView.VIEW_ID);

		// Single selection of a model element is OK.
		if ((selection != null) && (selection instanceof RequirementsModelProjectTreeElement))
			return true;

		// Otherwise it has to be an array (multiple selection).
		if (!(selection instanceof Object[]))
			return false;

		// Checks if the array contains only model elements.
		Object[] multipleSelection = (Object[]) selection;
		for (Object obj : multipleSelection)
			if (!(obj instanceof RequirementsModelProjectTreeElement))
				return false;

		// If it was an array and all elements were model elements, then it's OK.
		return true;
	}

	/**
	 * Retrieves the single selected model from the workspace.
	 * 
	 * Note that this method does not check if only a single model was selected, returning null if that is not the case.
	 * 
	 * @param selectionService
	 *          The platform's selection service, used to determine what is the user's current workspace selection.
	 * @return The single-selected model form the workspace, or <code>null</code> if a single model was not selected.
	 */
	public IFile retrieveSingleSelectedModel(ESelectionService selectionService) {
		// First checks that there is a single model element selected.
		Object selection = selectionService.getSelection(UnagiProjectExplorerView.VIEW_ID);
		if ((selection == null) || (!(selection instanceof RequirementsModelProjectTreeElement)))
			return null;

		// Then retrieves the model to which the selected element refers.
		RequirementsModelProjectTreeElement element = (RequirementsModelProjectTreeElement) selection;
		return element.getRequirementsFile();
	}

	/**
	 * Retrieves all selected models from the workspace.
	 * 
	 * Note that this method does not check if at least one model was selected, returning an empty set if that is not the
	 * case.
	 * 
	 * @param selectionService
	 *          The platform's selection service, used to determine what is the user's current workspace selection.
	 * @return A set containing all models that are currently selected in the workspace.
	 */
	public Set<IFile> retrieveMultipleSelectedModels(ESelectionService selectionService) {
		Set<IFile> models = new HashSet<>();
		Object selection = selectionService.getSelection(UnagiProjectExplorerView.VIEW_ID);

		// First checks if single selection, returning a set with a single element in this case.
		if ((selection != null) && (selection instanceof RequirementsModelProjectTreeElement)) {
			IFile model = ((RequirementsModelProjectTreeElement) selection).getRequirementsFile();
			models.add(model);
		}

		// Otherwise it has to be an array (multiple selection). Add all models.
		else if (selection instanceof Object[]) {
			Object[] multipleSelection = (Object[]) selection;
			for (Object obj : multipleSelection) {
				if (obj instanceof RequirementsModelProjectTreeElement) {
					IFile model = ((RequirementsModelProjectTreeElement) obj).getRequirementsFile();
					models.add(model);
				}
			}
		}

		return models;
	}

	/**
	 * Executes the command for a single-selected model.
	 * 
	 * Note that this method does not check if only a single model was selected, doing nothing if that is not the case.
	 * 
	 * @param selectionService
	 *          The platform's selection service, used to determine what is the user's current workspace selection.
	 */
	public void executeForSingleModel(ESelectionService selectionService) {
		IFile model = retrieveSingleSelectedModel(selectionService);
		if (model != null)
			handleSingleModel(model);
	}

	/**
	 * Executes the command for all selected models.
	 * 
	 * Note that this method does not check if at least one model was selected, doing nothing if that is not the case.
	 * 
	 * @param selectionService
	 *          The platform's selection service, used to determine what is the user's current workspace selection.
	 */
	public void executeForMultipleModels(ESelectionService selectionService) {
		Set<IFile> models = retrieveMultipleSelectedModels(selectionService);
		for (IFile model : models) {
			handleSingleModel(model);
		}
	}

	/**
	 * Internal template method that is called by the execute methods above, which performs the generic part of command
	 * handling and delegates the specific part of the command to the concrete subclass via the doExecute() method.
	 * 
	 * @param model
	 *          The model to handle.
	 */
	protected void handleSingleModel(final IFile model) {
		final String name = model.getName();
		final String cmdName = getClass().getName();
		LogUtil.log.debug("Executing \"{0}\" command for model: {1}", cmdName, name); //$NON-NLS-1$

		// Creates and schedules the job that will execute the command.
		Job job = new Job(getJobDescription(name)) {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					// Delegates the execution to the extending class.
					doExecute(monitor, model);
				}
				catch (UnagiException e) {
					LogUtil.log.error("The \"{0}\" command was unable to be performed for model: {1}.", e, cmdName, name); //$NON-NLS-1$

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
	 * Abstract method called by the template method handleSingleModel() so the specific part of command handling can be
	 * delegated to the concrete subclass.
	 * 
	 * @param monitor
	 *          The workbench's progress monitor, in case it's a long-running task.
	 * @param model
	 *          The model to handle.
	 * @throws UnagiException
	 *           If anything goes wrong during the specific handling of the model.
	 */
	protected abstract void doExecute(IProgressMonitor monitor, IFile model) throws UnagiException;
}
