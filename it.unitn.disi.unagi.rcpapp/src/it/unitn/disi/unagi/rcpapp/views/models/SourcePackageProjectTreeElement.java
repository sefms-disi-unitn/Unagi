package it.unitn.disi.unagi.rcpapp.views.models;

import it.unitn.disi.unagi.application.services.IManageSourcesService;
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
 * Project tree element that represents a package in the sources folder.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class SourcePackageProjectTreeElement extends AbstractProjectTreeElement {
	/** Path to the icon that should be displayed for elements of this type. */
	private static final String ICON_PATH = Messages.getIconPath("object.package.16"); //$NON-NLS-1$

	/** Project under which this element appears. */
	private IProject project;

	/** Name of the package. */
	private String name;

	/** Actual workspace folder to which this element refers. */
	private IFolder folder;

	/** Direct parent element in the project tree. */
	private SourcesFolderProjectTreeElement parent;

	/** Constructor. */
	public SourcePackageProjectTreeElement(Bundle bundle, IProject project, SourcesFolderProjectTreeElement parent, String name, IFolder folder) {
		super(bundle);
		LogUtil.log.debug("Creating a tree element for package {0} in project {1}.", name, project.getName()); //$NON-NLS-1$
		this.project = project;
		this.parent = parent;
		this.name = name;
		this.folder = folder;
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
		List<SourceFileProjectTreeElement> sourceFiles = new ArrayList<>();
		try {
			// Places all file resources under the models folder in a list.
			if (folder.exists())
				for (IResource resource : folder.members())
					if ((resource.getType() == IResource.FILE) && (resource.getFullPath().getFileExtension().equals(IManageSourcesService.SOURCE_FILE_EXTENSION)))
						sourceFiles.add(new SourceFileProjectTreeElement(bundle, project, this, (IFile) resource));

			// Convert the list to an array to return.
			children = sourceFiles.toArray();
		}
		catch (CoreException e) {
			LogUtil.log.error("Unagi caught an Eclipse error when retrieving the members of models folder: {0}/{1}.", e, project.getName(), folder.getName()); //$NON-NLS-1$
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
			hasChildren = folder.members().length > 0;
		}
		catch (CoreException e) {
			LogUtil.log.error("Unagi caught an Eclipse error when retrieving the members of package {0} in project {1}.", e, name, project.getName()); //$NON-NLS-1$
		}
		return hasChildren;
	}

	/** @see it.unitn.disi.unagi.rcpapp.views.models.AbstractProjectTreeElement#getText() */
	@Override
	public String getText() {
		// Return the name of the package.
		return name;
	}

	/** @see it.unitn.disi.unagi.rcpapp.views.models.AbstractProjectTreeElement#getImage() */
	@Override
	public Image getImage() {
		// Return the icon that represents packages.
		return ImageUtil.loadImage(bundle, ICON_PATH);
	}
}
