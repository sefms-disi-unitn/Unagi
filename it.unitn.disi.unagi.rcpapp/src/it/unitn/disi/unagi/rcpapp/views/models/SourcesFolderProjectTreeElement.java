package it.unitn.disi.unagi.rcpapp.views.models;

import it.unitn.disi.unagi.application.services.IManageProjectsService;
import it.unitn.disi.unagi.application.services.IManageSourcesService;
import it.unitn.disi.unagi.rcpapp.nls.Messages;
import it.unitn.disi.util.gui.ImageUtil;
import it.unitn.disi.util.logging.LogUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.Bundle;

/**
 * Project tree element that represents a folder containing source files.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class SourcesFolderProjectTreeElement extends AbstractProjectTreeElement {
	/** Name that should be displayed by this element in the tree. */
	private static final String NAME = Messages.getString("object.sources"); //$NON-NLS-1$

	/** Path to the icon that should be displayed for elements of this type. */
	private static final String ICON_PATH = Messages.getIconPath("object.sources.16"); //$NON-NLS-1$

	/** Project under which this element appears. */
	private IProject project;

	/** Actual workspace folder to which this element refers. */
	private IFolder sourcesFolder;

	/** Direct parent element in the project tree. */
	private ProjectProjectTreeElement parent;

	/** Constructor. */
	public SourcesFolderProjectTreeElement(Bundle bundle, IProject project, ProjectProjectTreeElement parent) {
		super(bundle);
		LogUtil.log.debug("Creating a tree element for the sources folder of project: {0}.", project.getName()); //$NON-NLS-1$
		this.project = project;
		this.parent = parent;
		sourcesFolder = project.getFolder(IManageProjectsService.SOURCES_PROJECT_SUBDIR);
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
		List<SourcePackageProjectTreeElement> javaPackages = new ArrayList<>();
		try {
			// Finds all packages that contain at least one class.
			Map<String, IFolder> packagesMap = scanSourcesFolderForPackages();

			// For each package, create a project tree element that represents it.
			for (Map.Entry<String, IFolder> entry : packagesMap.entrySet()) {
				SourcePackageProjectTreeElement packageElem = new SourcePackageProjectTreeElement(bundle, project, this, entry.getKey(), entry.getValue());
				javaPackages.add(packageElem);
			}

			// Convert the list to an array to return.
			children = javaPackages.toArray();
		}
		catch (CoreException e) {
			LogUtil.log.error("Unagi caught an Eclipse error when retrieving the members of sources folder: {0}/{1}.", project.getName(), sourcesFolder.getName()); //$NON-NLS-1$
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
			hasChildren = sourcesFolder.members().length > 0;
		}
		catch (CoreException e) {
			LogUtil.log.error("Unagi caught an Eclipse error when retrieving the members of sources folder: {0}/{1}.", e, project.getName(), sourcesFolder.getName()); //$NON-NLS-1$
		}
		return hasChildren;
	}

	/** @see it.unitn.disi.unagi.rcpapp.views.models.AbstractProjectTreeElement#getText() */
	@Override
	public String getText() {
		// Return the term that represents sources.
		return NAME;
	}

	/** @see it.unitn.disi.unagi.rcpapp.views.models.AbstractProjectTreeElement#getImage() */
	@Override
	public Image getImage() {
		// Return the icon that represents sources.
		return ImageUtil.loadImage(bundle, ICON_PATH);
	}

	/**
	 * Scans the source folder and identifies all folders containing source files. Assembles a map that associates the
	 * package name (in Java-style fully qualified name) with the folder.
	 * 
	 * @return A tree map that associates package names to their folders in the project.
	 * @throws CoreException
	 *           If traversing the folders looking for Java classes throws an Eclipse exception.
	 */
	private Map<String, IFolder> scanSourcesFolderForPackages() throws CoreException {
		String projectName = project.getName();
		LogUtil.log.debug("Traversing the sources folder of project {0} looking for packages.", projectName); //$NON-NLS-1$
		Map<String, IFolder> packagesMap = new TreeMap<>();

		// Traverses the folders looking for classes.
		Set<IFile> sources = recursivelyFindJavaSources(sourcesFolder);
		for (IFile sourceFile : sources) {
			// Extracts package information from the path of the source file.
			IPath packagePath = sourceFile.getFullPath().removeFirstSegments(1).removeLastSegments(1);

			// Extracts a Java-style fully qualified name for the package from its path.
			String packageName = packagePath.removeFirstSegments(1).toString().replace('/', '.');

			// Verifies if the package is already in the map. If not, add it.
			if (!packagesMap.containsKey(packageName)) {
				IFolder packageFolder = project.getFolder(packagePath);
				packagesMap.put(packageName, packageFolder);
			}
		}

		// Returns the map of packages.
		LogUtil.log.debug("Number of packages found in project {0}: {1}.", projectName, packagesMap.size()); //$NON-NLS-1$
		return packagesMap;
	}

	/**
	 * Traverses the given folder recursively and returns all source files found in the current folder or any of its
	 * sub-folders.
	 * 
	 * @param folder
	 *          The folder to traverse recursively.
	 * @return A hash set of IFile elements, representing the source files that were found.
	 * @throws CoreException
	 *           If Eclipse cannot list the members of a given IFolder object.
	 */
	private Set<IFile> recursivelyFindJavaSources(IFolder folder) throws CoreException {
		LogUtil.log.debug("Traversing a source folder looking for source files: {0}.", folder.getFullPath()); //$NON-NLS-1$
		Set<IFile> sources = new HashSet<>();

		// Goes through the members of the folder.
		int count = 0;
		if (folder.exists())
			for (IResource resource : folder.members())
				// Checks if it's a file or a folder.
				switch (resource.getType()) {
				case IResource.FILE:
					// If it's a file, checks that it is a source, then adds it to the sources set.
					if (resource.getFullPath().getFileExtension().equals(IManageSourcesService.SOURCE_FILE_EXTENSION)) {
						sources.add((IFile) resource);
						count++;
					}
					break;

				case IResource.FOLDER:
					// If it's a folder, recursively find classes in it.
					Set<IFile> moreSources = recursivelyFindJavaSources((IFolder) resource);
					sources.addAll(moreSources);
					break;
				}

		// Return all sources found.
		LogUtil.log.debug("Found {0} source file(s) in folder {1}.", count, folder.getFullPath()); //$NON-NLS-1$
		return sources;
	}
}
