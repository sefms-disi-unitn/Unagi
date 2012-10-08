package it.unitn.disi.unagi.application.internal.services;

import it.unitn.disi.unagi.application.exceptions.CouldNotCloseUnagiProjectException;
import it.unitn.disi.unagi.application.exceptions.CouldNotOpenUnagiProjectException;
import it.unitn.disi.unagi.application.exceptions.CouldNotSaveUnagiProjectException;
import it.unitn.disi.unagi.application.services.IManageProjectsService;
import it.unitn.disi.unagi.natures.UnagiProjectNature;
import it.unitn.disi.util.logging.LogUtil;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Implementation for the service class for project management.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class ManageProjectsService implements IManageProjectsService {
	/**
	 * @see it.unitn.disi.unagi.application.services.IManageProjectsService#createNewProject(org.eclipse.core.runtime.IProgressMonitor,
	 *      java.lang.String)
	 */
	@Override
	public IProject createNewProject(IProgressMonitor progressMonitor, String name) throws CouldNotSaveUnagiProjectException {
		LogUtil.log.info("Creating a new project: {0}.", name); //$NON-NLS-1$

		// Obtains a project descriptor with the desired name from the workspace.
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = root.getProject(name);

		// If the project already exists, throws a Unagi exception.
		if (project.exists()) {
			LogUtil.log.warn("A project with the same name already exists in the workspace: {0}.", name); //$NON-NLS-1$
			throw new CouldNotSaveUnagiProjectException(project);
		}

		try {
			// Creates and opens the project.
			project.create(progressMonitor);
			project.open(progressMonitor);

			// Sets the Unagi project nature.
			IProjectDescription description = project.getDescription();
			String[] natures = description.getNatureIds();
			String[] newNatures = new String[natures.length + 1];
			System.arraycopy(natures, 0, newNatures, 0, natures.length);
			newNatures[natures.length] = UnagiProjectNature.NATURE_ID;
			description.setNatureIds(newNatures);
			project.setDescription(description, progressMonitor);

			// Creates the project's subfolders.
			for (String subdir : PROJECT_SUBDIRS) {
				IFolder folder = project.getFolder(subdir);
				folder.create(true, true, progressMonitor);
			}
		}

		// If any other errors occurred, throw an exception.
		catch (CoreException e) {
			LogUtil.log.error("Unagi caught an Eclipse exception while trying to create project: {0}.", e, name); //$NON-NLS-1$
			throw new CouldNotSaveUnagiProjectException(project, e);
		}

		LogUtil.log.info("Successfully created and opened Unagi project: {0}.", name); //$NON-NLS-1$
		return project;
	}

	/**
	 * @see it.unitn.disi.unagi.application.services.IManageProjectsService#closeProject(org.eclipse.core.runtime.IProgressMonitor,
	 *      org.eclipse.core.resources.IProject)
	 */
	@Override
	public void closeProject(IProgressMonitor progressMonitor, IProject project) throws CouldNotCloseUnagiProjectException {
		String name = project.getName();
		LogUtil.log.info("Closing project: {0}.", name); //$NON-NLS-1$

		// Attempts to close the project.
		try {
			project.close(progressMonitor);
		}

		// If any other errors occurred, throw an exception.
		catch (CoreException e) {
			LogUtil.log.error("Unagi caught an Eclipse exception while trying to close project: {0}.", e, name); //$NON-NLS-1$
			throw new CouldNotCloseUnagiProjectException(project, e);
		}
	}

	/**
	 * @see it.unitn.disi.unagi.application.services.IManageProjectsService#openProject(org.eclipse.core.runtime.IProgressMonitor,
	 *      org.eclipse.core.resources.IProject)
	 */
	@Override
	public void openProject(IProgressMonitor progressMonitor, IProject project) throws CouldNotOpenUnagiProjectException {
		String name = project.getName();
		LogUtil.log.info("Opening project: {0}.", name); //$NON-NLS-1$

		// Attempts to close the project.
		try {
			project.open(progressMonitor);
		}

		// If any other errors occurred, throw an exception.
		catch (CoreException e) {
			LogUtil.log.error("Unagi caught an Eclipse exception while trying to open project: {0}.", e, name); //$NON-NLS-1$
			throw new CouldNotOpenUnagiProjectException(project, e);
		}
	}
}
