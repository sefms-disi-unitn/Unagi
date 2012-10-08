package it.unitn.disi.unagi.rcpapp.wizards;

import it.unitn.disi.unagi.application.exceptions.UnagiException;
import it.unitn.disi.unagi.application.services.IManageProjectsService;
import it.unitn.disi.unagi.rcpapp.IUnagiRcpAppBundleInfoProvider;
import it.unitn.disi.unagi.rcpapp.nls.Messages;
import it.unitn.disi.util.gui.DialogUtil;
import it.unitn.disi.util.logging.LogUtil;

import java.util.List;

import javax.inject.Inject;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

/**
 * Wizard that guides the user in the process of opening a Unagi project.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
@Creatable
public class OpenProjectsWizard extends Wizard implements INewWizard {
	/** The bundle's activator, used to retrieve global information on the RCP application. */
	@Inject
	protected IUnagiRcpAppBundleInfoProvider activator;

	/** Service class for project management. */
	@Inject
	private IManageProjectsService manageProjectsService;

	/** Wizard page that shows the closed projects that exist in the workspace. */
	@Inject
	private OpenProjectsSelectProjectsWizardPage selectProjectPage;

	/** Constructor. */
	public OpenProjectsWizard() {
		LogUtil.log.debug("Creating a \"Open Project(s)\" wizard."); //$NON-NLS-1$
		setNeedsProgressMonitor(true);
		setWindowTitle(Messages.getString("gui.openProjectsWizard.title")); //$NON-NLS-1$
	}

	/** @see org.eclipse.jface.wizard.Wizard#addPages() */
	@Override
	public void addPages() {
		// Adds the basic page instance to the wizard.
		addPage(selectProjectPage);
	}

	/** @see org.eclipse.jface.wizard.Wizard#performFinish() */
	@Override
	public boolean performFinish() {
		LogUtil.log.debug("Finishing a \"Open Project(s)\" wizard."); //$NON-NLS-1$

		// Retrieves the information about the project.
		final List<IProject> selectedProjects = selectProjectPage.getSelectedProjects();

		// Creates and schedules the job that will open the project.
		Job job = new Job(Messages.getFormattedString("service.openProject.description", selectedProjects.size())) { //$NON-NLS-1$
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				for (IProject project : selectedProjects) {
					String name = project.getName();

					try {
						// Opens the project.
						manageProjectsService.openProject(monitor, project);
					}
					catch (UnagiException e) {
						LogUtil.log.error("The \"Open Project(s)\" wizard was unable to open a project.", e); //$NON-NLS-1$

						// If the project could not be opened, shows an error message.
						final String statusMsg = Messages.getString("service.openProject.error.status"); //$NON-NLS-1$
						final String errorTitle = Messages.getString("service.openProject.error.title"); //$NON-NLS-1$
						final String errorMessage = Messages.getFormattedString("service.openProject.error.message", name); //$NON-NLS-1$
						return DialogUtil.displayError(activator.getBundleId(), statusMsg, errorTitle, errorMessage);
					}
				}

				return Status.OK_STATUS;
			}
		};
		job.setUser(true);
		job.schedule();

		return true;
	}

	/** @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench, org.eclipse.jface.viewers.IStructuredSelection) */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {}
}
