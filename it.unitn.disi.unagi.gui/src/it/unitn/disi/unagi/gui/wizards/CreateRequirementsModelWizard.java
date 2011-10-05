package it.unitn.disi.unagi.gui.wizards;

import it.unitn.disi.unagi.application.exceptions.UnagiException;
import it.unitn.disi.unagi.application.services.ManageModelsService;
import it.unitn.disi.unagi.application.services.Unagi;
import it.unitn.disi.unagi.domain.core.UnagiProject;

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
public class CreateRequirementsModelWizard extends Wizard implements INewWizard {
	/** Wizard page that asks for the basic information on the project. */
	private CreateRequirementsModelBasicWizardPage basicPage;
	
	/** Project in which the requriements model will be created. */
	private UnagiProject project;
	
	/** Constructor. */
	public CreateRequirementsModelWizard(UnagiProject project) {
		this.project = project;
		setNeedsProgressMonitor(true);
		setWindowTitle("Create new Requirements Model");
	}

	/** @see org.eclipse.jface.wizard.Wizard#addPages() */
	@Override
	public void addPages() {
		// Creates the basic page instance and adds it to the wizard.
		basicPage = new CreateRequirementsModelBasicWizardPage(project);
		addPage(basicPage);
	}

	/** @see org.eclipse.jface.wizard.Wizard#performFinish() */
	@Override
	public boolean performFinish() {
		ManageModelsService manageModelsService = Unagi.getInstance().getManageModelsService();
		
		// Creates a new requirements model with the name that was specified in the wizard.
		String name = basicPage.getModelName();
		try {
			manageModelsService.createNewRequirementsModel(project, name);
		}
		catch (UnagiException e) {
			// In case of any application exception, show an error message.
			Status status = new Status(IStatus.ERROR, "it.unitn.disi.unagi.gui", "The requirements model could not be created.");
			ErrorDialog.openError(this.getShell(), "Error while creating a new requirements model", "Could not create a new requirements. Please try again or contact support.", status);
		}
		
		return true;
	}

	/** @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench, org.eclipse.jface.viewers.IStructuredSelection) */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {}
}
