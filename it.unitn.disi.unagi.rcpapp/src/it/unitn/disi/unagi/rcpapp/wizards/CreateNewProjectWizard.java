package it.unitn.disi.unagi.rcpapp.wizards;

import it.unitn.disi.unagi.application.exceptions.UnagiException;
import it.unitn.disi.unagi.application.services.IManageProjectsService;
import it.unitn.disi.unagi.rcpapp.IUnagiRcpAppBundleInfoProvider;
import it.unitn.disi.unagi.rcpapp.nls.Messages;
import it.unitn.disi.util.gui.DialogUtil;
import it.unitn.disi.util.logging.LogUtil;

import javax.inject.Inject;

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
 * Wizard that guides the user in the process of creating a new Unagi project.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
@Creatable
public class CreateNewProjectWizard extends Wizard implements INewWizard {
	/** The bundle's activator, used to retrieve global information on the RCP application. */
	@Inject
	protected IUnagiRcpAppBundleInfoProvider activator;

	/** Service class for project management. */
	@Inject
	private IManageProjectsService manageProjectsService;

	/** Wizard page that asks for the basic information on the project. */
	@Inject
	private CreateNewProjectBasicWizardPage basicPage;

	/** Constructor. */
	public CreateNewProjectWizard() {
		LogUtil.log.debug("Creating a \"Create New Project\" wizard."); //$NON-NLS-1$
		setNeedsProgressMonitor(true);
		setWindowTitle(Messages.getString("gui.createNewProjectWizard.title")); //$NON-NLS-1$
	}

	/** @see org.eclipse.jface.wizard.Wizard#addPages() */
	@Override
	public void addPages() {
		// Adds the basic page instance to the wizard.
		addPage(basicPage);
	}

	/** @see org.eclipse.jface.wizard.Wizard#performFinish() */
	@Override
	public boolean performFinish() {
		LogUtil.log.debug("Finishing a \"Create New Project\" wizard."); //$NON-NLS-1$

		// Retrieves the information about the project.
		final String name = basicPage.getProjectName();

		// Creates and schedules the job that will create and open the new project.
		Job job = new Job(Messages.getFormattedString("service.createNewProject.description", name)) { //$NON-NLS-1$
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					// Creates the new project.
					manageProjectsService.createNewProject(monitor, name);
				}
				catch (UnagiException e) {
					LogUtil.log.error("The \"Create New Project\" wizard was unable to create a new project.", e); //$NON-NLS-1$

					// If the project could not be saved, show an error message.
					final String statusMsg = Messages.getString("service.createNewProject.error.status"); //$NON-NLS-1$
					final String errorTitle = Messages.getString("service.createNewProject.error.title"); //$NON-NLS-1$
					final String errorMessage = Messages.getFormattedString("service.createNewProject.error.message", name); //$NON-NLS-1$
					return DialogUtil.displayError(activator.getBundleId(), statusMsg, errorTitle, errorMessage);
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
