package it.unitn.disi.unagi.rcpapp.views.models;

import it.unitn.disi.unagi.application.services.IManageProjectsService;
import it.unitn.disi.unagi.rcpapp.nls.Messages;
import it.unitn.disi.util.gui.ImageUtil;
import it.unitn.disi.util.logging.LogUtil;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.Bundle;

/**
 * Project tree element that represents a folder containing requirements models.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class RequirementsFolderProjectTreeElement extends AbstractProjectTreeElement {
	/** Name that should be displayed by this element in the tree. */
	private static final String NAME = Messages.getString("object.models"); //$NON-NLS-1$

	/** Path to the icon that should be displayed for elements of this type. */
	private static final String ICON_PATH = Messages.getIconPath("object.models.16"); //$NON-NLS-1$

	/** Project under which this element appears. */
	private IProject project;

	/** Actual workspace folder to which this element refers. */
	private IFolder modelsFolder;

	/** Direct parent element in the project tree. */
	private ProjectProjectTreeElement parent;

	/** Constructor. */
	public RequirementsFolderProjectTreeElement(Bundle bundle, IProject project, ProjectProjectTreeElement parent) {
		super(bundle);
		LogUtil.log.debug("Creating a tree element for the models folder of project: {0}.", project.getName()); //$NON-NLS-1$
		this.project = project;
		this.parent = parent;
		this.modelsFolder = project.getFolder(IManageProjectsService.MODELS_PROJECT_SUBDIR);
	}

	/** @see it.unitn.disi.unagi.rcpapp.views.models.AbstractProjectTreeElement#getProject() */
	@Override
	public IProject getProject() {
		return project;
	}

	/** @see it.unitn.disi.unagi.rcpapp.views.models.AbstractProjectTreeElement#getChildren() */
	@Override
	public Object[] getChildren() {
		Object[] children = null;
		List<RequirementsModelProjectTreeElement> requirementsFiles = new ArrayList<>();
		try {
			// Places all file resources under the models folder in a list.
			if (modelsFolder.exists())
				for (IResource resource : modelsFolder.members())
					if (resource.getType() == IResource.FILE)
						requirementsFiles.add(new RequirementsModelProjectTreeElement(bundle, project, (IFile) resource, this));

			// Convert the list to an array to return.
			children = requirementsFiles.toArray();
		}
		catch (CoreException e) {
			LogUtil.log.error("Unagi caught an Eclipse error when retrieving the members of models folder: {0}/{1}.", project.getName(), modelsFolder.getName()); //$NON-NLS-1$
		}
		return children;
	}

	/** @see it.unitn.disi.unagi.rcpapp.views.models.AbstractProjectTreeElement#getParent() */
	@Override
	public Object getParent() {
		return parent;
	}

	/** @see it.unitn.disi.unagi.rcpapp.views.models.AbstractProjectTreeElement#hasChildren() */
	@Override
	public boolean hasChildren() {
		boolean hasChildren = false;
		try {
			hasChildren = modelsFolder.members().length > 0;
		}
		catch (CoreException e) {
			LogUtil.log.error("Unagi caught an Eclipse error when retrieving the members of models folder: {0}/{1}.", project.getName(), modelsFolder.getName()); //$NON-NLS-1$
		}
		return hasChildren;
	}

	/** @see it.unitn.disi.unagi.rcpapp.views.models.AbstractProjectTreeElement#getText() */
	@Override
	public String getText() {
		// Return the term that represents requirements.
		return NAME;
	}

	/** @see it.unitn.disi.unagi.rcpapp.views.models.AbstractProjectTreeElement#getImage() */
	@Override
	public Image getImage() {
		// Return the icon that represents requirements.
		return ImageUtil.loadImage(bundle, ICON_PATH);
	}

	/** @see it.unitn.disi.unagi.rcpapp.views.models.AbstractProjectTreeElement#getPopupMenuId() */
	@Override
	public String getPopupMenuId() {
		return POPUP_MENU_PREFIX + "requirementsFolder"; //$NON-NLS-1$
	}
}
