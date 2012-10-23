package it.unitn.disi.unagi.rcpapp.views.models;

import it.unitn.disi.unagi.rcpapp.nls.Messages;
import it.unitn.disi.util.gui.ImageUtil;
import it.unitn.disi.util.logging.LogUtil;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.Bundle;

/**
 * Project tree element that represents a project.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class ProjectProjectTreeElement extends AbstractProjectTreeElement {
	/** Path to the icon that should be displayed for elements of this type. */
	private static final String ICON_PATH = Messages.getIconPath("object.project.16"); //$NON-NLS-1$

	/** ID of the pop-up menu that should appear if an element of this type is right-clicked. */
	private static final String PROJECT_POPUP_MENU_ID = POPUP_MENU_PREFIX + "project"; //$NON-NLS-1$

	/** Actual workspace project to which this element refers. */
	private IProject project;

	/** Children of this project, i.e., the different folder holding different project elements such as models. */
	private AbstractProjectTreeElement[] children;

	/** Constructor. */
	public ProjectProjectTreeElement(Bundle bundle, IProject project) {
		super(bundle);
		this.project = project;
		LogUtil.log.debug("Creating a tree element for project: {0}.", project.getName()); //$NON-NLS-1$

		// TODO: issue #15
		// https://github.com/sefms-disi-unitn/Unagi/issues/15

		// Build the project's children.
		children = new AbstractProjectTreeElement[2];
		children[0] = new ModelsFolderProjectTreeElement(bundle, project, this);
		children[1] = new SourcesFolderProjectTreeElement(bundle, project, this);
	}

	/** @see it.unitn.disi.unagi.rcpapp.views.models.AbstractProjectTreeElement#getProject() */
	@Override
	public IProject getProject() {
		return project;
	}

	/** @see it.unitn.disi.unagi.rcpapp.views.models.AbstractProjectTreeElement#getChildren() */
	@Override
	public Object[] getChildren() {
		// Return the project's children.
		return children;
	}

	/** @see it.unitn.disi.unagi.rcpapp.views.models.AbstractProjectTreeElement#hasChildren() */
	@Override
	public boolean hasChildren() {
		return true;
	}

	/** @see it.unitn.disi.unagi.rcpapp.views.models.AbstractProjectTreeElement#getText() */
	@Override
	public String getText() {
		// Return the name of the project.
		return project.getName();
	}

	/** @see it.unitn.disi.unagi.rcpapp.views.models.AbstractProjectTreeElement#getImage() */
	@Override
	public Image getImage() {
		// Return the icon that represents projects.
		return ImageUtil.loadImage(bundle, ICON_PATH);
	}

	/** @see it.unitn.disi.unagi.rcpapp.views.models.AbstractProjectTreeElement#getPopupMenuId() */
	@Override
	public String getPopupMenuId() {
		return PROJECT_POPUP_MENU_ID;
	}
}
