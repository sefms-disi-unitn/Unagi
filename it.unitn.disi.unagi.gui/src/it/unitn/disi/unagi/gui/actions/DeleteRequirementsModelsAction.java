package it.unitn.disi.unagi.gui.actions;

import it.unitn.disi.unagi.application.exceptions.UnagiException;
import it.unitn.disi.unagi.application.services.ManageModelsService;
import it.unitn.disi.unagi.application.services.Unagi;
import it.unitn.disi.unagi.domain.core.RequirementsModel;
import it.unitn.disi.unagi.gui.utils.ImageUtil;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;

/**
 * Action class that can be associated with a menu item and allows the user to delete a requirements model.
 *
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class DeleteRequirementsModelsAction extends Action {
	/** Requirements models to be deleted. */
	private List<RequirementsModel> models;

	/** Constructor. */
	public DeleteRequirementsModelsAction(List<RequirementsModel> models) {
		this.models = models;
		
		// Sets the text to show in the context menu.
		String s = (models.size() > 1) ? "s" : ""; 
		setText("Delete requirements model" + s);
		setToolTipText("Deletes the selected requirements model " + s);
		
		// Sets the icon to show in the context menu.
		Image icon = ImageUtil.loadImage("/icons/action-deleterequirementsmodels.png");
		setImageDescriptor(ImageDescriptor.createFromImage(icon));
	}

	/** @see org.eclipse.jface.action.Action#run() */
	@Override
	public void run() {
		// When this action is run, ask for confirmation on deleting the models and delete them.
		StringBuilder builder = new StringBuilder();
		builder.append("Are you sure you want to delete the selected requirement model").append((models.size() > 1) ? "s" : "").append("?\n\n");
		for (RequirementsModel model : models)
			builder.append("- ").append(model.getName()).append(";\n");
		builder.append("\nWarning: the model's file will be deleted from the file system. This action cannot be undone.");
		
		// Show the confirmation message.
		MessageBox msgBox = new MessageBox(Display.getCurrent().getActiveShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
		msgBox.setMessage(builder.toString());
		msgBox.setText("Confirm deletion?");
		int answer = msgBox.open();
		
		// If the answer is yes, delete the models.
		if (answer == SWT.YES) {
			Unagi unagi = Unagi.getInstance();
			ManageModelsService manageModelsService = unagi.getManageModelsService();
			for (RequirementsModel model : models)
				try {
					manageModelsService.deleteRequirementsModel(model);
				}
			catch (UnagiException e) {
				// In case of any application exception, show an error message.
				Status status = new Status(IStatus.ERROR, "it.unitn.disi.unagi.gui", "The requirements model could not be deleted: " + model.getName());
				ErrorDialog.openError(Display.getCurrent().getActiveShell(), "Error while deleting a requirements model", "Could not delete requirements model \"" + model.getName() + "\". Please try again or contact support.", status);
			}
		}
	}
}
