package it.unitn.disi.unagi.gui.commands;

import it.unitn.disi.unagi.application.exceptions.UnagiException;
import it.unitn.disi.unagi.application.services.ManageProjectsService;
import it.unitn.disi.unagi.application.services.Unagi;
import it.unitn.disi.unagi.gui.Activator;
import it.unitn.disi.unagi.gui.nls.Messages;

import java.io.File;
import java.text.MessageFormat;

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
		dialog.setText(Messages.getString("gui.command.newProject.misc.dialogTitle")); //$NON-NLS-1$
		dialog.setMessage(Messages.getString("gui.command.newProject.misc.dialogMessage")); //$NON-NLS-1$
		String selectedDirPath = dialog.open();
		
		// Checks if the user has selected a directory.
		if (selectedDirPath != null) {
			File selectedDir = new File(selectedDirPath);
			
			// Checks if the directory is a project directory and opens it. Deals with possible errors.
			if (manageProjectsService.isProjectFolder(selectedDir))
				try {
					manageProjectsService.loadProject(selectedDir);
				}
				catch (UnagiException e) {
					// If the project could not be loaded from the specified folder, show an error message.
					String statusMsg = Messages.getString("gui.command.newProject.error.status"); //$NON-NLS-1$
					String errorTitle = Messages.getString("gui.command.newProject.error.title"); //$NON-NLS-1$
					MessageFormat errorMsg = new MessageFormat(Messages.getString("gui.command.newProject.error.message")); //$NON-NLS-1$
					Object[] args = new Object[] { selectedDirPath };
					Status status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, statusMsg);
					ErrorDialog.openError(shell, errorTitle, errorMsg.format(args), status);
				}
		}
		
		return null;
	}
}
