package it.unitn.disi.unagi.gui.actions;

import it.unitn.disi.unagi.application.exceptions.UnagiException;
import it.unitn.disi.unagi.application.services.ManageModelsService;
import it.unitn.disi.unagi.application.services.Unagi;
import it.unitn.disi.unagi.domain.core.RequirementsModel;
import it.unitn.disi.unagi.gui.Activator;
import it.unitn.disi.unagi.gui.nls.Messages;
import it.unitn.disi.unagi.gui.utils.ImageUtil;

import java.text.MessageFormat;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * Action class that can be associated with a menu item and allows the user to delete a requirements model.
 *
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class CompileRequirementsModelsAction extends Action {
	/** Requirements models to be deleted. */
	private List<RequirementsModel> models;

	/** Constructor. */
	public CompileRequirementsModelsAction(List<RequirementsModel> models) {
		this.models = models;
		
		// Sets the text to show in the context menu.
		MessageFormat textMsg = new MessageFormat(Messages.getString("gui.action.compileRequirementsModels.text")); //$NON-NLS-1$
		MessageFormat tooltipMsg = new MessageFormat(Messages.getString("gui.action.compileRequirementsModels.tooltip")); //$NON-NLS-1$
		Object[] args = new Object[] { models.size() };
		setText(textMsg.format(args));
		setToolTipText(tooltipMsg.format(args));
		
		// Sets the icon to show in the context menu.
		Image icon = ImageUtil.loadImage("/icons/action-compilerequirementsmodels.png"); //$NON-NLS-1$
		setImageDescriptor(ImageDescriptor.createFromImage(icon));
	}

	/** @see org.eclipse.jface.action.Action#run() */
	@Override
	public void run() {
		Unagi unagi = Unagi.getInstance();
		ManageModelsService manageModelsService = unagi.getManageModelsService();
		for (RequirementsModel model : models)
			try {
				manageModelsService.compileRequirementsModel(model);
			}
		catch (UnagiException e) {
			// In case of any application exception, show an error message.
			MessageFormat statusMsg = new MessageFormat(Messages.getString("gui.action.compileRequirementsModels.error.status")); //$NON-NLS-1$
			MessageFormat errorMsg = new MessageFormat(Messages.getString("gui.action.compileRequirementsModels.error.message")); //$NON-NLS-1$
			String dialogTitle = Messages.getString("gui.action.compileRequirementsModels.error.title"); //$NON-NLS-1$
			Object[] args = new Object[] { model.getName() };
			Status status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, statusMsg.format(args));
			ErrorDialog.openError(Display.getCurrent().getActiveShell(), dialogTitle, errorMsg.format(args), status);
		}
	}
}
