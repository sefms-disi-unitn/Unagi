package it.unitn.disi.unagi.application.services;

import it.unitn.disi.unagi.application.exceptions.CouldNotCloseUnagiProjectException;
import it.unitn.disi.unagi.application.exceptions.CouldNotOpenUnagiProjectException;
import it.unitn.disi.unagi.application.exceptions.CouldNotSaveUnagiProjectException;
import it.unitn.disi.unagi.application.nls.Messages;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Interface for the project management service, which allows the user to create, save and load Unagi projects.
 * 
 * Services contain application business logic that is GUI-independent and should be registered by the application
 * bundle in OSGi for proper dependency injection in the GUI classes provided by the main RCPApp bundle.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public interface IManageProjectsService {
	/** Name of the project sub-directory that will hold the project's models. */
	String MODELS_PROJECT_SUBDIR = Messages.getString("fs.models.folderName"); //$NON-NLS-1$
	
	/** name of the project sub-directory that will hold the project's source code files. */
	String SOURCES_PROJECT_SUBDIR = Messages.getString("fs.sources.folderName"); //$NON-NLS-1$
	
	/** Name of the project sub-directory that will hold the project's compiled classes. */
	String CLASSES_PROJECT_SUBDIR = Messages.getString("fs.classes.folderName"); //$NON-NLS-1$

	/** List of names of sub-directories that should exist/be created for every Unagi project. */
	String[] PROJECT_SUBDIRS = new String[] { MODELS_PROJECT_SUBDIR, SOURCES_PROJECT_SUBDIR, CLASSES_PROJECT_SUBDIR };

	/**
	 * Creates a new Unagi project, given some basic (mandatory) information about it.
	 * 
	 * @param progressMonitor
	 *          The platform's progress monitor.
	 * @param name
	 *          Project name.
	 * @return The newly created project.
	 * @throws CouldNotSaveUnagiProjectException
	 *           If for some reason the new project could not be saved (e.g., I/O error on the specified folder).
	 */
	IProject createNewProject(IProgressMonitor progressMonitor, String name) throws CouldNotSaveUnagiProjectException;

	/**
	 * Closes a Unagi project.
	 * 
	 * @param progressMonitor
	 *          The platform's progress monitor.
	 * @param project
	 *          The project to be closed.
	 * @throws CouldNotCloseUnagiProjectException
	 *           If for some internal Eclipse reason the project could not be closed.
	 */
	void closeProject(IProgressMonitor progressMonitor, IProject project) throws CouldNotCloseUnagiProjectException;

	/**
	 * Opens a Unagi project.
	 * 
	 * @param progressMonitor
	 * @param project
	 * @throws CouldNotOpenUnagiProjectException
	 */
	void openProject(IProgressMonitor progressMonitor, IProject project) throws CouldNotOpenUnagiProjectException;
}
