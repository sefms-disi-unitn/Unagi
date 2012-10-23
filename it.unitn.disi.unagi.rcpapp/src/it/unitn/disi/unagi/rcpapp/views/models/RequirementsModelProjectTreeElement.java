package it.unitn.disi.unagi.rcpapp.views.models;

import it.unitn.disi.unagi.rcpapp.nls.Messages;
import it.unitn.disi.util.gui.ImageUtil;
import it.unitn.disi.util.logging.LogUtil;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.Bundle;

/**
 * Project tree model element that represents a requirements model.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class RequirementsModelProjectTreeElement extends ModelProjectTreeElement {
	/** Path to the icon that should be displayed for elements of this type. */
	private static final String ICON_PATH = Messages.getIconPath("object.requirements.16"); //$NON-NLS-1$

	/** ID of the pop-up menu that should appear if an element of this type is right-clicked. */
	private static final String REQUIREMENTS_MODEL_POPUP_MENU_ID = POPUP_MENU_PREFIX + "requirementsModel"; //$NON-NLS-1$

	/** ID of the default command for double-clicks on an element of this type. */
	private static final String REQUIREMENTS_MODEL_DEFAULT_COMMAND_ID = "it.unitn.disi.unagi.rcpapp.command.openRequirementsModels"; //$NON-NLS-1$

	/** Project under which this element appears. */
	private IProject project;

	/** Actual workspace file to which this element refers. */
	private IFile requirementsFile;

	/** Direct parent element in the project tree. */
	private ModelsFolderProjectTreeElement parent;

	/** Constructor. */
	public RequirementsModelProjectTreeElement(Bundle bundle, IProject project, ModelsFolderProjectTreeElement parent, IFile requirementsFile) {
		super(bundle);
		LogUtil.log.debug("Creating a tree element for a requirements file of project: {0}.", project.getName()); //$NON-NLS-1$
		this.project = project;
		this.parent = parent;
		this.requirementsFile = requirementsFile;
	}

	/** @see it.unitn.disi.unagi.rcpapp.views.models.AbstractProjectTreeElement#getProject() */
	@Override
	public IProject getProject() {
		return project;
	}

	/** @see it.unitn.disi.unagi.rcpapp.views.models.AbstractProjectTreeElement#getParent() */
	@Override
	public Object getParent() {
		return parent;
	}

	/** @see it.unitn.disi.unagi.rcpapp.views.models.AbstractProjectTreeElement#getText() */
	@Override
	public String getText() {
		// Return the term that represents requirements.
		return requirementsFile.getName();
	}

	/** @see it.unitn.disi.unagi.rcpapp.views.models.AbstractProjectTreeElement#getImage() */
	@Override
	public Image getImage() {
		// Return the icon that represents requirements.
		return ImageUtil.loadImage(bundle, ICON_PATH);
	}

	/** Getter for requirementsFile. */
	public IFile getRequirementsFile() {
		return requirementsFile;
	}

	/** @see it.unitn.disi.unagi.rcpapp.views.models.AbstractProjectTreeElement#getPopupMenuId() */
	@Override
	public String getPopupMenuId() {
		return REQUIREMENTS_MODEL_POPUP_MENU_ID;
	}

	/** @see it.unitn.disi.unagi.rcpapp.views.models.AbstractProjectTreeElement#getDefaultCommandId() */
	@Override
	public String getDefaultCommandId() {
		return REQUIREMENTS_MODEL_DEFAULT_COMMAND_ID;
	}
}
