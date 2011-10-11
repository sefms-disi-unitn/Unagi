package it.unitn.disi.unagi.gui.actions;

import it.unitn.disi.unagi.application.services.ManageProjectsService;
import it.unitn.disi.unagi.application.services.Unagi;
import it.unitn.disi.unagi.domain.core.UnagiProject;
import it.unitn.disi.unagi.gui.nls.Messages;
import it.unitn.disi.unagi.gui.utils.ImageUtil;

import java.text.MessageFormat;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

/**
 * Action class that can be associated with a menu item and allows the user to close a project.
 *
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class CloseProjectsAction extends Action {
	/** The Unagi application. */
	private Unagi unagi = Unagi.getInstance();
	
	/** The "Manage Projects" service. */
	private ManageProjectsService manageProjectsService = unagi.getManageProjectsService();

	/** Unagi Projects to close if this action is executed. */
	private List<UnagiProject> projects;

	/** Constructor. */
	public CloseProjectsAction(List<UnagiProject> projects) {
		this.projects = projects;
		
		// Sets the text to show in the context menu.
		MessageFormat textMsg = new MessageFormat(Messages.getString("gui.action.closeProject.text")); //$NON-NLS-1$
		MessageFormat tooltipMsg = new MessageFormat(Messages.getString("gui.action.closeProject.tooltip")); //$NON-NLS-1$
		Object[] args = new Object[] { projects.size() };
		setText(textMsg.format(args));
		setToolTipText(tooltipMsg.format(args));
		
		// Sets the icon to show in the context menu.
		Image icon = ImageUtil.loadImage("/icons/action-closeproject.png"); //$NON-NLS-1$
		setImageDescriptor(ImageDescriptor.createFromImage(icon));
	}

	/** @see org.eclipse.jface.action.Action#run() */
	@Override
	public void run() {
		// When this action is run, closes the selected projects.
		for (UnagiProject project : projects) manageProjectsService.closeProject(project);
	}
}
