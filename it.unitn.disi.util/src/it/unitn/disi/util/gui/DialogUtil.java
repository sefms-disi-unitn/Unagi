package it.unitn.disi.util.gui;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Display;

/**
 * Utility class for displaying dialogs in the application's GUI.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public final class DialogUtil {
	/**
	 * Displays an error dialog.
	 * 
	 * @param bundleId
	 *          The ID of the bundle which is responsible for the error message.
	 * @param statusMsg
	 *          The status message related to the error, used to create the status object that is returned.
	 * @param errorTitle
	 *          The error title.
	 * @param errorMessage
	 *          The error message.
	 * @return The status object representing the occurrence of this error.
	 */
	public static IStatus displayError(String bundleId, final String statusMsg, final String errorTitle, final String errorMessage) {
		// Creates the error status that will be returned.
		final Status status = new Status(IStatus.ERROR, bundleId, statusMsg);

		// Displays the error dialog in the current active shell.
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				ErrorDialog.openError(Display.getCurrent().getActiveShell(), errorTitle, errorMessage, status);
			}
		});

		// Returns the error status.
		return status;
	}
}
