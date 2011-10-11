package it.unitn.disi.unagi.gui.actions;

import it.unitn.disi.unagi.application.exceptions.UnagiException;
import it.unitn.disi.unagi.application.services.ManageModelsService;
import it.unitn.disi.unagi.application.services.Unagi;
import it.unitn.disi.unagi.domain.core.RequirementsModel;
import it.unitn.disi.unagi.gui.nls.Messages;
import it.unitn.disi.unagi.gui.utils.ImageUtil;

import java.text.MessageFormat;
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
		MessageFormat textMsg = new MessageFormat(Messages.getString("gui.action.deleteRequirementsModels.text")); //$NON-NLS-1$
		MessageFormat tooltipMsg = new MessageFormat(Messages.getString("gui.action.deleteRequirementsModels.tooltip")); //$NON-NLS-1$
		Object[] args = new Object[] { models.size() };
		setText(textMsg.format(args));
		setToolTipText(tooltipMsg.format(args));
		
		// Sets the icon to show in the context menu.
		Image icon = ImageUtil.loadImage("/icons/action-deleterequirementsmodels.png"); //$NON-NLS-1$
		setImageDescriptor(ImageDescriptor.createFromImage(icon));
	}

	/** @see org.eclipse.jface.action.Action#run() */
	@Override
	public void run() {
		// When this action is run, ask for confirmation on deleting the models and delete them.
		MessageFormat confirmationMsg = new MessageFormat(Messages.getString("gui.action.deleteRequirementsModels.misc.confirmationText")); //$NON-NLS-1$
		StringBuilder builder = new StringBuilder();
		for (RequirementsModel model : models)
			builder.append("- ").append(model.getName()).append(";\n"); //$NON-NLS-1$ //$NON-NLS-2$
		Object[] args = new Object[] { models.size(), builder.toString() };
		
		// Show the confirmation message.
		MessageBox msgBox = new MessageBox(Display.getCurrent().getActiveShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
		msgBox.setMessage(confirmationMsg.format(args));
		msgBox.setText(Messages.getString("gui.action.deleteRequirementsModels.misc.confirmationTitle")); //$NON-NLS-1$
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
				MessageFormat statusMsg = new MessageFormat(Messages.getString("gui.action.deleteRequirementsModels.error.status")); //$NON-NLS-1$
				MessageFormat errorMsg = new MessageFormat(Messages.getString("gui.action.deleteRequirementsModels.error.message")); //$NON-NLS-1$
				String dialogTitle = Messages.getString("gui.action.deleteRequirementsModels.error.title"); //$NON-NLS-1$
				args = new Object[] { model.getName() };
				Status status = new Status(IStatus.ERROR, "it.unitn.disi.unagi.gui", statusMsg.format(args)); //$NON-NLS-1$
				ErrorDialog.openError(Display.getCurrent().getActiveShell(), dialogTitle, errorMsg.format(args), status);
			}
		}
	}
}
