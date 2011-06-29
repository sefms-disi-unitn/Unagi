package it.unitn.disi.unagi.gui.wizards;

import it.unitn.disi.unagi.application.exceptions.CouldNotSaveUnagiProjectException;
import it.unitn.disi.unagi.application.services.ManageProjectsService;
import it.unitn.disi.unagi.application.services.Unagi;

import java.io.File;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;


/**
 * The "Create Project" wizard, which guides the user in the creation of a new Unagi Project.
 *
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class CreateProjectWizard extends Wizard implements INewWizard {
	/** Wizard page that asks for the basic information on the project. */
	private CreateProjectBasicWizardPage basicPage;
	
	/** Constructor. */
	public CreateProjectWizard() {
		setNeedsProgressMonitor(true);
		setWindowTitle("Create new Unagi Project");
	}

	/** @see org.eclipse.jface.wizard.Wizard#addPages() */
	@Override
	public void addPages() {
		// Creates the basic page instance and adds it to the wizard.
		basicPage = new CreateProjectBasicWizardPage();
		addPage(basicPage);
	}

	/** @see org.eclipse.jface.wizard.Wizard#performFinish() */
	@Override
	public boolean performFinish() {
		ManageProjectsService manageProjectsService = Unagi.getInstance().getManageProjectsService();
		
		// Creates a new Unagi Project with the name and folder that were specified in the wizard.
		String name = basicPage.getProjectName();
		File folder = new File(basicPage.getFolderPath());
		try {
			manageProjectsService.createNewProject(name, folder);
		}
		catch (CouldNotSaveUnagiProjectException e) {
			// If the project could not be saved to the specified folder, show an error message.
			Status status = new Status(IStatus.ERROR, "it.unitn.disi.unagi.gui", "The Unagi Project could not be saved.");
			ErrorDialog.openError(this.getShell(), "Error while creating a new Unagi Project", "Could not create a new Unagi Project. Please try again or contact support.", status);
		}
		
		return true;
	}

	/** @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench, org.eclipse.jface.viewers.IStructuredSelection) */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {}
}
