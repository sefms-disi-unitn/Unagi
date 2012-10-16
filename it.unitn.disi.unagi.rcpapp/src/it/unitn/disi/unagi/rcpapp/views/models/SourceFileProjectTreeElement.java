package it.unitn.disi.unagi.rcpapp.views.models;

import it.unitn.disi.unagi.rcpapp.nls.Messages;
import it.unitn.disi.util.gui.ImageUtil;
import it.unitn.disi.util.logging.LogUtil;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.Bundle;

/**
 * Project tree element that represents a source file in one of the packages of the sources folder.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class SourceFileProjectTreeElement extends AbstractProjectTreeElement {
	/** Path to the icon that should be displayed for elements of this type. */
	private static final String ICON_PATH = Messages.getIconPath("object.source.16"); //$NON-NLS-1$

	/** ID of the pop-up menu that should appear if an element of this type is right-clicked. */
	private static final String SOURCE_FILE_POPUP_MENU_ID = POPUP_MENU_PREFIX + "sourceFile"; //$NON-NLS-1$
	
	/** ID of the default command for double-clicks on an element of this type. */
	private static final String SOURCE_FILE_DEFAULT_COMMAND_ID = "it.unitn.disi.unagi.rcpapp.command.openSourceFiles"; //$NON-NLS-1$

	/** Project under which this element appears. */
	private IProject project;

	/** Actual workspace file to which this element refers. */
	private IFile sourceFile;

	/** Direct parent element in the project tree. */
	private SourcePackageProjectTreeElement parent;

	/** Constructor. */
	public SourceFileProjectTreeElement(Bundle bundle, IProject project, SourcePackageProjectTreeElement parent, IFile sourceFile) {
		super(bundle);
		LogUtil.log.debug("Creating a tree element for the source file {0} of project {1}.", sourceFile.getName(), project.getName()); //$NON-NLS-1$
		this.project = project;
		this.parent = parent;
		this.sourceFile = sourceFile;
	}

	/** Getter for sourceFile. */
	public IFile getSourceFile() {
		return sourceFile;
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
		return sourceFile.getFullPath().removeFileExtension().lastSegment();
	}

	/** @see it.unitn.disi.unagi.rcpapp.views.models.AbstractProjectTreeElement#getImage() */
	@Override
	public Image getImage() {
		// Return the icon that represents packages.
		return ImageUtil.loadImage(bundle, ICON_PATH);
	}

	/** @see it.unitn.disi.unagi.rcpapp.views.models.AbstractProjectTreeElement#getPopupMenuId() */
	@Override
	public String getPopupMenuId() {
		return SOURCE_FILE_POPUP_MENU_ID;
	}

	/** @see it.unitn.disi.unagi.rcpapp.views.models.AbstractProjectTreeElement#getDefaultCommandId() */
	@Override
	public String getDefaultCommandId() {
		return SOURCE_FILE_DEFAULT_COMMAND_ID;
	}
}
