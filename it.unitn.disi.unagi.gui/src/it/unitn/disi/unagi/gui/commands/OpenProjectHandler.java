package it.unitn.disi.unagi.gui.commands;

import it.unitn.disi.unagi.application.exceptions.CouldNotLoadUnagiProjectException;
import it.unitn.disi.unagi.application.services.ManageProjectsService;
import it.unitn.disi.unagi.application.services.Unagi;

import java.io.File;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * A handler for the "Open Project..." command. Allows the user to open an existing project.
 * 
 * @author Vítor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class OpenProjectHandler extends AbstractHandler {
	/** The Unagi application. */
	private Unagi unagi = Unagi.getInstance();
	
	/** The "Manage Projects" service. */
	private ManageProjectsService manageProjectsService = unagi.getManageProjectsService();

	/** @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent) */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// Opens a directory dialog and asks the user to specify the project folder.
		Shell shell = HandlerUtil.getActiveShell(event);
		DirectoryDialog dialog = new DirectoryDialog(shell);
		dialog.setFilterPath(unagi.getProperty(Unagi.CFG_LAST_FOLDER_FILE_DIALOGS));
		dialog.setText("Open an existing Unagi Project");
		dialog.setMessage("Select a project's folder:");
		String selectedDirPath = dialog.open();
		
		// Checks if the user has selected a directory.
		if (selectedDirPath != null) {
			File selectedDir = new File(selectedDirPath);
			
			// Checks if the directory is a project directory and opens it. Deals with possible errors.
			if (manageProjectsService.isProjectFolder(selectedDir))
				try {
					manageProjectsService.loadProject(selectedDir);
				}
				catch (CouldNotLoadUnagiProjectException e) {
					// If the project could not be loaded from the specified folder, show an error message.
					Status status = new Status(IStatus.ERROR, "it.unitn.disi.unagi.gui", "The Unagi Project could not be opened.");
					ErrorDialog.openError(shell, "Error while opening an existing Unagi Project", "Could not open a Unagi Project from folder \"" + selectedDirPath + "\". Please try again or contact support.", status);
				}
		}
		
		return null;
	}
}
