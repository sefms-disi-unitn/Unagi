package it.unitn.disi.unagi.gui.actions;

import java.text.MessageFormat;

import it.unitn.disi.unagi.domain.core.UnagiProject;
import it.unitn.disi.unagi.gui.nls.Messages;
import it.unitn.disi.unagi.gui.utils.ImageUtil;
import it.unitn.disi.unagi.gui.wizards.CreateRequirementsModelWizard;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * Action class that can be associated with a menu item and allows the user to create a new requirements model.
 *
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class NewRequirementsModelAction extends Action {
	/** Unagi Project in which to create a new requirements model. */
	private UnagiProject project;

	/** Constructor. */
	public NewRequirementsModelAction(UnagiProject project) {
		this.project = project;
		
		// Sets the text to show in the context menu.
		MessageFormat tooltipMsg = new MessageFormat(Messages.getString("gui.action.newRequirementsModel.tooltip")); //$NON-NLS-1$
		Object[] args = new Object[] { project.getName() };
		setText(Messages.getString("gui.action.newRequirementsModel.text")); //$NON-NLS-1$
		setToolTipText(tooltipMsg.format(args));
		
		// Sets the icon to show in the context menu.
		Image icon = ImageUtil.loadImage("/icons/action-newrequirementsmodel.png"); //$NON-NLS-1$
		setImageDescriptor(ImageDescriptor.createFromImage(icon));
	}

	/** @see org.eclipse.jface.action.Action#run() */
	@Override
	public void run() {
		// When this action is run, open the "Create requirements model" wizard.
		CreateRequirementsModelWizard wizard = new CreateRequirementsModelWizard(project);
		WizardDialog dialog = new WizardDialog(Display.getCurrent().getActiveShell(), wizard);
		dialog.open();
	}
}
