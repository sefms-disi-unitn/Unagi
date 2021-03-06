package it.unitn.disi.unagi.rcpapp.wizards;

import it.unitn.disi.unagi.application.exceptions.UnagiException;
import it.unitn.disi.unagi.application.services.IManageModelsService;
import it.unitn.disi.unagi.rcpapp.IUnagiRcpAppBundleInfoProvider;
import it.unitn.disi.unagi.rcpapp.nls.Messages;
import it.unitn.disi.util.gui.DialogUtil;
import it.unitn.disi.util.logging.LogUtil;

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
 * The "Create New Constraints File" wizard, which guides the user in the creation of a new constraints file.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
@Creatable
public class CreateNewConstraintsFileWizard extends Wizard implements INewWizard {
	/** The bundle's activator, used to retrieve global information on the RCP application. */
	@Inject
	protected IUnagiRcpAppBundleInfoProvider activator;

	/** Service class for constraints file management. */
	@Inject
	private IManageModelsService manageModelsService;

	/** Wizard page that asks for the basic information on the project. */
	@Inject
	private CreateNewConstraintsFileBasicWizardPage basicPage;

	/** Constructor. */
	public CreateNewConstraintsFileWizard() {
		LogUtil.log.debug("Creating a \"Create New Constraints File\" wizard."); //$NON-NLS-1$
		setNeedsProgressMonitor(true);
		setWindowTitle(Messages.getString("gui.createNewConstraintsFileWizard.title")); //$NON-NLS-1$
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
		LogUtil.log.debug("Finishing a \"Create New Constraints File\" wizard."); //$NON-NLS-1$

		// Creates a new constraints file with the name that was specified in the wizard.
		final IProject project = basicPage.getSelectedProject();
		final String projectName = project.getName();
		final String name = basicPage.getModelName();

		// Creates and schedules the job that will create the new constraints file.
		Job job = new Job(Messages.getFormattedString("service.createNewConstraintsFile.description", name, projectName)) { //$NON-NLS-1$
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					// Creates the new project.
					manageModelsService.createNewConstraintsFile(monitor, project, name);
				}
				catch (UnagiException e) {
					LogUtil.log.error("The \"Create New Constraints File\" wizard was unable to create a new constraints file.", e); //$NON-NLS-1$

					// If the project could not be saved, show an error message.
					final String statusMsg = Messages.getString("service.createNewConstraintsFile.error.status"); //$NON-NLS-1$
					final String errorTitle = Messages.getString("service.createNewConstraintsFile.error.title"); //$NON-NLS-1$
					final String errorMessage = Messages.getFormattedString("service.createNewConstraintsFile.error.message", name, projectName); //$NON-NLS-1$
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
