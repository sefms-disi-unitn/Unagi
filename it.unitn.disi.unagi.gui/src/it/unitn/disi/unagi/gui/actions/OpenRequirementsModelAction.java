package it.unitn.disi.unagi.gui.actions;

import it.unitn.disi.unagi.domain.core.RequirementsModel;
import it.unitn.disi.unagi.gui.Activator;
import it.unitn.disi.unagi.gui.nls.Messages;
import it.unitn.disi.unagi.gui.utils.ImageUtil;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.FileStoreEditorInput;

/**
 * Action class that can be associated with a menu item and allows the user to open a requirements model.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class OpenRequirementsModelAction extends Action {
	/** Requirements models to open if this action is executed. */
	private List<RequirementsModel> models;

	/** Constructor. */
	public OpenRequirementsModelAction(List<RequirementsModel> models) {
		this.models = models;

		// Sets the text to show in the context menu.
		MessageFormat textMsg = new MessageFormat(Messages.getString("gui.action.openRequirementsModel.text")); //$NON-NLS-1$
		MessageFormat tooltipMsg = new MessageFormat(Messages.getString("gui.action.openRequirementsModel.tooltip")); //$NON-NLS-1$
		Object[] args = new Object[] { models.size() };
		setText(textMsg.format(args));
		setToolTipText(tooltipMsg.format(args));

		// Sets the icon to show in the context menu.
		Image icon = ImageUtil.loadImage("/icons/action-openrequirementsmodel.png"); //$NON-NLS-1$
		setImageDescriptor(ImageDescriptor.createFromImage(icon));
	}
	
	/** Constructor. */
	public OpenRequirementsModelAction(RequirementsModel model) {
		this(Arrays.asList(new RequirementsModel[] { model }));
	}

	/** @see org.eclipse.jface.action.Action#run() */
	@Override
	public void run() {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				
		// When this action is run, open the selected models.
		for (RequirementsModel model : models) {
			IFileStore fileStore = EFS.getLocalFileSystem().getStore(new Path(model.getFile().getAbsolutePath()));
			IEditorInput input = new FileStoreEditorInput(fileStore);
			try {
				// TODO: programmatically load the LTL.ecore and goalmodel.ecore files!
				//IEditorPart editor = 
						page.openEditor(input, "org.eclipse.emf.ecore.presentation.EcoreEditorID"); //$NON-NLS-1$
				//EcoreEditor ecoreEditor = (EcoreEditor) editor;
			}
			catch (PartInitException e) {
				// In case the editor part cannot be initialized, shows an error dialog.
				MessageFormat statusMsg = new MessageFormat(Messages.getString("gui.action.openRequirementsModelAction.error.status")); //$NON-NLS-1$
				MessageFormat errorMsg = new MessageFormat(Messages.getString("gui.action.openRequirementsModelAction.error.message")); //$NON-NLS-1$
				String dialogTitle = Messages.getString("gui.action.openRequirementsModelAction.error.title"); //$NON-NLS-1$
				Object[] args = new Object[] { model.getName() };
				Status status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, statusMsg.format(args));
				ErrorDialog.openError(Display.getCurrent().getActiveShell(), dialogTitle, errorMsg.format(args), status);
			}
		}
	}
}
