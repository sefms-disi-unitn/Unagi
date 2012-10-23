package it.unitn.disi.unagi.rcpapp.handlers;

import it.unitn.disi.unagi.application.exceptions.UnagiException;
import it.unitn.disi.unagi.rcpapp.views.UnagiProjectExplorerView;
import it.unitn.disi.unagi.rcpapp.views.models.RulesFileProjectTreeElement;
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
 * Abstract class for command handlers in the RCP App bundle that deal with one or more rules file in the execution of
 * the command. Contains methods that check and manipulate the selected rules files in the workspace, delegating the
 * specific execution to the concrete subclass.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public abstract class AbstractRulesFilesHandler extends AbstractHandler {
	/**
	 * Verifies if a single rules file is selected in the workspace. Should be used by handlers whose commands operate in
	 * one and only one rules file.
	 * 
	 * @param selectionService
	 *          The platform's selection service, used to determine what is the user's current workspace selection.
	 * @return <code>true</code> if there's exactly one rules file selected, <code>false</code> otherwise.
	 */
	public boolean isSingleRulesSelected(ESelectionService selectionService) {
		Object selection = selectionService.getSelection(UnagiProjectExplorerView.VIEW_ID);
		return ((selection != null) && (selection instanceof RulesFileProjectTreeElement));
	}

	/**
	 * Verifies if at least one rules file is selected in the workspace. Should be used by handlers whose commands operate
	 * in one or more rules file at once.
	 * 
	 * @param selectionService
	 *          The platform's selection service, used to determine what is the user's current workspace selection.
	 * @return <code>true</code> if at least one rules file is selected, <code>false</code> otherwise.
	 */
	public boolean isAtLeastOneRulesSelected(ESelectionService selectionService) {
		Object selection = selectionService.getSelection(UnagiProjectExplorerView.VIEW_ID);

		// Single selection of a project tree element is OK.
		if ((selection != null) && (selection instanceof RulesFileProjectTreeElement))
			return true;

		// Otherwise it has to be an array (multiple selection).
		if (!(selection instanceof Object[]))
			return false;

		// Checks if the array contains only rules elements.
		Object[] multipleSelection = (Object[]) selection;
		for (Object obj : multipleSelection)
			if (!(obj instanceof RulesFileProjectTreeElement))
				return false;

		// If it was an array and all elements were rules?, then it's OK.
		return true;
	}

	/**
	 * Retrieves the single selected rules file from the workspace.
	 * 
	 * Note that this method does not check if only a single rules file was selected, returning null if that is not the
	 * case.
	 * 
	 * @param selectionService
	 *          The platform's selection service, used to determine what is the user's current workspace selection.
	 * @return The single-selected rules file form the workspace, or <code>null</code> if a single rules file was not
	 *         selected.
	 */
	public IFile retrieveSingleSelectedRules(ESelectionService selectionService) {
		// First checks that there is a single rules element selected.
		Object selection = selectionService.getSelection(UnagiProjectExplorerView.VIEW_ID);
		if ((selection == null) || (!(selection instanceof RulesFileProjectTreeElement)))
			return null;

		// Then retrieves the file to which the selected element refers.
		RulesFileProjectTreeElement element = (RulesFileProjectTreeElement) selection;
		return element.getRulesFile();
	}

	/**
	 * Retrieves all selected rules files from the workspace.
	 * 
	 * Note that this method does not check if at least one rules file was selected, returning an empty set if that is not
	 * the case.
	 * 
	 * @param selectionService
	 *          The platform's selection service, used to determine what is the user's current workspace selection.
	 * @return A set containing all rules files that are currently selected in the workspace.
	 */
	public Set<IFile> retrieveMultipleSelectedRules(ESelectionService selectionService) {
		Set<IFile> rules = new HashSet<>();
		Object selection = selectionService.getSelection(UnagiProjectExplorerView.VIEW_ID);

		// First checks if single selection of rules, returning a set with a single element in this case.
		if ((selection != null) && (selection instanceof RulesFileProjectTreeElement)) {
			IFile model = ((RulesFileProjectTreeElement) selection).getRulesFile();
			rules.add(model);
		}

		// Otherwise it has to be an array (multiple selection). Add all rules (single and package).
		else if (selection instanceof Object[]) {
			Object[] multipleSelection = (Object[]) selection;
			for (Object obj : multipleSelection) {
				if (obj instanceof RulesFileProjectTreeElement) {
					IFile model = ((RulesFileProjectTreeElement) obj).getRulesFile();
					rules.add(model);
				}
			}
		}

		return rules;
	}

	/**
	 * Executes the command for a single-selected rules file.
	 * 
	 * Note that this method does not check if only a single rules file was selected, doing nothing if that is not the
	 * case.
	 * 
	 * @param selectionService
	 *          The platform's selection service, used to determine what is the user's current workspace selection.
	 */
	public void executeForSingleRules(ESelectionService selectionService) {
		IFile rules = retrieveSingleSelectedRules(selectionService);
		if (rules != null)
			handleSingleRules(rules);
	}

	/**
	 * Executes the command for all selected rules files.
	 * 
	 * Note that this method does not check if at least one rules file was selected, doing nothing if that is not the
	 * case.
	 * 
	 * @param selectionService
	 *          The platform's selection service, used to determine what is the user's current workspace selection.
	 */
	public void executeForMultipleRules(ESelectionService selectionService) {
		Set<IFile> rules = retrieveMultipleSelectedRules(selectionService);
		for (IFile model : rules) {
			handleSingleRules(model);
		}
	}

	/**
	 * Internal template method that is called by the execute methods above, which performs the generic part of command
	 * handling and delegates the specific part of the command to the concrete subclass via the doExecute() method.
	 * 
	 * @param rules
	 *          The rules file to handle.
	 */
	protected void handleSingleRules(final IFile rules) {
		final String name = rules.getName();
		final String cmdName = getClass().getName();
		LogUtil.log.debug("Executing \"{0}\" command for rules file: {1}", cmdName, name); //$NON-NLS-1$

		// Creates and schedules the job that will execute the command.
		Job job = new Job(getJobDescription(name)) {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					// Delegates the execution to the extending class.
					doExecute(monitor, rules);
				}
				catch (UnagiException e) {
					LogUtil.log.error("The \"{0}\" command was unable to be performed for rules file: {1}.", e, cmdName, name); //$NON-NLS-1$

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
	 * Abstract method called by the template method handleSingleRules() so the specific part of command handling can be
	 * delegated to the concrete subclass.
	 * 
	 * @param monitor
	 *          The workbench's progress monitor, in case it's a long-running task.
	 * @param rules
	 *          The rules file to handle.
	 * @throws UnagiException
	 *           If anything goes wrong during the specific handling of the rules file.
	 */
	protected abstract void doExecute(IProgressMonitor monitor, IFile rules) throws UnagiException;
}
