package it.unitn.disi.unagi.rcpapp.handlers;

import it.unitn.disi.unagi.application.exceptions.UnagiException;
import it.unitn.disi.unagi.rcpapp.views.UnagiProjectExplorerView;
import it.unitn.disi.unagi.rcpapp.views.models.SourcePackageProjectTreeElement;
import it.unitn.disi.unagi.rcpapp.views.models.SourceFileProjectTreeElement;
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
 * Abstract class for command handlers in the RCP App bundle that deal with one or more sources in the execution of the
 * command. Contains methods that check and manipulate the selected sources in the workspace, delegating the specific
 * execution to the concrete subclass.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public abstract class AbstractSourceFilesHandler extends AbstractHandler {
	/**
	 * Verifies if a single source is selected in the workspace. Should be used by handlers whose commands operate in one
	 * and only one source.
	 * 
	 * @param selectionService
	 *          The platform's selection service, used to determine what is the user's current workspace selection.
	 * @return <code>true</code> if there's exactly one source selected, <code>false</code> otherwise.
	 */
	public boolean isSingleSourceSelected(ESelectionService selectionService) {
		Object selection = selectionService.getSelection(UnagiProjectExplorerView.VIEW_ID);
		return ((selection != null) && (selection instanceof SourceFileProjectTreeElement));
	}

	/**
	 * Verifies if at least one source is selected in the workspace. Should be used by handlers whose commands operate in
	 * one or more sources at once.
	 * 
	 * Note that if a package is selected, we consider that all of its sources are selected.
	 * 
	 * @param selectionService
	 *          The platform's selection service, used to determine what is the user's current workspace selection.
	 * @return <code>true</code> if at least one source is selected, <code>false</code> otherwise.
	 */
	public boolean isAtLeastOneSourceSelected(ESelectionService selectionService) {
		Object selection = selectionService.getSelection(UnagiProjectExplorerView.VIEW_ID);

		// Single selection of a project tree element is OK.
		if ((selection != null) && ((selection instanceof SourceFileProjectTreeElement) || (selection instanceof SourcePackageProjectTreeElement)))
			return true;

		// Otherwise it has to be an array (multiple selection).
		if (!(selection instanceof Object[]))
			return false;

		// Checks if the array contains only sources and package elements.
		Object[] multipleSelection = (Object[]) selection;
		for (Object obj : multipleSelection)
			if (!(obj instanceof SourceFileProjectTreeElement) && !(obj instanceof SourcePackageProjectTreeElement))
				return false;

		// If it was an array and all elements were sources/packages, then it's OK.
		return true;
	}

	/**
	 * Retrieves the single selected source from the workspace.
	 * 
	 * Note that this method does not check if only a single source was selected, returning null if that is not the case.
	 * 
	 * @param selectionService
	 *          The platform's selection service, used to determine what is the user's current workspace selection.
	 * @return The single-selected source form the workspace, or <code>null</code> if a single source was not selected.
	 */
	public IFile retrieveSingleSelectedSource(ESelectionService selectionService) {
		// First checks that there is a single source element selected.
		Object selection = selectionService.getSelection(UnagiProjectExplorerView.VIEW_ID);
		if ((selection == null) || (!(selection instanceof SourceFileProjectTreeElement)))
			return null;

		// Then retrieves the file to which the selected element refers.
		SourceFileProjectTreeElement element = (SourceFileProjectTreeElement) selection;
		return element.getSourceFile();
	}

	/**
	 * Retrieves all selected sources from the workspace.
	 * 
	 * Note that this method does not check if at least one source was selected, returning an empty set if that is not the
	 * case.
	 * 
	 * @param selectionService
	 *          The platform's selection service, used to determine what is the user's current workspace selection.
	 * @return A set containing all sources that are currently selected in the workspace.
	 */
	public Set<IFile> retrieveMultipleSelectedSources(ESelectionService selectionService) {
		Set<IFile> sources = new HashSet<>();
		Object selection = selectionService.getSelection(UnagiProjectExplorerView.VIEW_ID);

		// First checks if single selection of source, returning a set with a single element in this case.
		if ((selection != null) && (selection instanceof SourceFileProjectTreeElement)) {
			IFile model = ((SourceFileProjectTreeElement) selection).getSourceFile();
			sources.add(model);
		}

		// Then checks if single selection of package, returning a set with all package classes.
		else if ((selection != null) && (selection instanceof SourcePackageProjectTreeElement)) {
			addSourcesFromPackage((SourcePackageProjectTreeElement) selection, sources);
		}

		// Otherwise it has to be an array (multiple selection). Add all sources (single and package).
		else if (selection instanceof Object[]) {
			Object[] multipleSelection = (Object[]) selection;
			for (Object obj : multipleSelection) {
				if (obj instanceof SourceFileProjectTreeElement) {
					IFile model = ((SourceFileProjectTreeElement) obj).getSourceFile();
					sources.add(model);
				}
				else if (obj instanceof SourcePackageProjectTreeElement) {
					addSourcesFromPackage((SourcePackageProjectTreeElement) obj, sources);
				}
			}
		}

		return sources;
	}

	/**
	 * TODO: document this method.
	 * 
	 * @param pkgElem
	 * @param sources
	 */
	private void addSourcesFromPackage(SourcePackageProjectTreeElement pkgElem, Set<IFile> sources) {
		for (Object obj : pkgElem.getChildren()) {
			if (obj instanceof SourceFileProjectTreeElement) {
				IFile model = ((SourceFileProjectTreeElement) obj).getSourceFile();
				sources.add(model);
			}
		}
	}

	/**
	 * Executes the command for a single-selected source.
	 * 
	 * Note that this method does not check if only a single source was selected, doing nothing if that is not the case.
	 * 
	 * @param selectionService
	 *          The platform's selection service, used to determine what is the user's current workspace selection.
	 */
	public void executeForSingleSource(ESelectionService selectionService) {
		IFile source = retrieveSingleSelectedSource(selectionService);
		if (source != null)
			handleSingleSource(source);
	}

	/**
	 * Executes the command for all selected sources.
	 * 
	 * Note that this method does not check if at least one source was selected, doing nothing if that is not the case.
	 * 
	 * @param selectionService
	 *          The platform's selection service, used to determine what is the user's current workspace selection.
	 */
	public void executeForMultipleSources(ESelectionService selectionService) {
		Set<IFile> sources = retrieveMultipleSelectedSources(selectionService);
		for (IFile model : sources) {
			handleSingleSource(model);
		}
	}

	/**
	 * Internal template method that is called by the execute methods above, which performs the generic part of command
	 * handling and delegates the specific part of the command to the concrete subclass via the doExecute() method.
	 * 
	 * @param source
	 *          The source to handle.
	 */
	protected void handleSingleSource(final IFile source) {
		final String name = source.getName();
		final String cmdName = getClass().getName();
		LogUtil.log.debug("Executing \"{0}\" command for source: {1}", cmdName, name); //$NON-NLS-1$

		// Creates and schedules the job that will execute the command.
		Job job = new Job(getJobDescription(name)) {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					// Delegates the execution to the extending class.
					doExecute(monitor, source);
				}
				catch (UnagiException e) {
					LogUtil.log.error("The \"{0}\" command was unable to be performed for source: {1}.", e, cmdName, name); //$NON-NLS-1$

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
	 * Abstract method called by the template method handleSingleSource() so the specific part of command handling can be
	 * delegated to the concrete subclass.
	 * 
	 * @param monitor
	 *          The workbench's progress monitor, in case it's a long-running task.
	 * @param source
	 *          The source to handle.
	 * @throws UnagiException
	 *           If anything goes wrong during the specific handling of the source.
	 */
	protected abstract void doExecute(IProgressMonitor monitor, IFile source) throws UnagiException;
}
