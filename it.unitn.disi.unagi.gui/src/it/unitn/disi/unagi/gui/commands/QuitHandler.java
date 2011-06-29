package it.unitn.disi.unagi.gui.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * A handler for the "Quit" command. Quits the application.
 * 
 * @author Vítor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class QuitHandler extends AbstractHandler {
	/** @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent) */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// Closes the workbench window, effectively quitting the application.
		HandlerUtil.getActiveWorkbenchWindow(event).close();
		return null;
	}
}
