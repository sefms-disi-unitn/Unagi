package it.unitn.disi.unagi.rcpapp.views.models;

import it.unitn.disi.unagi.rcpapp.nls.Messages;
import it.unitn.disi.util.gui.ImageUtil;
import it.unitn.disi.util.logging.LogUtil;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.Bundle;

/**
 * Project tree element that represents a java source file in one of the packages of the Java sources folder.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class JavaSourceProjectTreeElement extends AbstractProjectTreeElement {
	/** Path to the icon that should be displayed for elements of this type. */
	private static final String ICON_PATH = Messages.getIconPath("object.source.16"); //$NON-NLS-1$

	/** Project under which this element appears. */
	private IProject project;

	/** Actual workspace file to which this element refers. */
	private IFile file;

	/** Direct parent element in the project tree. */
	private JavaPackageProjectTreeElement parent;

	/** Constructor. */
	public JavaSourceProjectTreeElement(Bundle bundle, IProject project, JavaPackageProjectTreeElement parent, IFile file) {
		super(bundle);
		LogUtil.log.debug("Creating a tree element for the Java source file {0} of project {1}.", file.getName(), project.getName()); //$NON-NLS-1$
		this.project = project;
		this.parent = parent;
		this.file = file;
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
		// Return the name of the file, without extension.
		return file.getFullPath().removeFileExtension().lastSegment();
	}

	/** @see it.unitn.disi.unagi.rcpapp.views.models.AbstractProjectTreeElement#getImage() */
	@Override
	public Image getImage() {
		// Return the icon that represents Java packages.
		return ImageUtil.loadImage(bundle, ICON_PATH);
	}
}
