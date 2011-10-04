package it.unitn.disi.unagi.gui.commands;

import it.unitn.disi.unagi.gui.wizards.CreateProjectWizard;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * A handler for the "New Project..." command. Allows the user to create a new project.
 * 
 * @author Vítor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class NewProjectHandler extends AbstractHandler {
	/** @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent) */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO: comment this code.
		CreateProjectWizard wizard = new CreateProjectWizard();
		WizardDialog dialog = new WizardDialog(HandlerUtil.getActiveShell(event), wizard);
		dialog.open();
		
		return null;
	}
}
