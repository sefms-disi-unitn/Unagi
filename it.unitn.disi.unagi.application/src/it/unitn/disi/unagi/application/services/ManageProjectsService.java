package it.unitn.disi.unagi.application.services;

import it.unitn.disi.unagi.application.exceptions.CouldNotLoadUnagiProjectException;
import it.unitn.disi.unagi.application.exceptions.CouldNotSaveUnagiProjectException;
import it.unitn.disi.unagi.domain.core.UnagiProject;

import java.io.File;
import java.util.Set;

import org.eclipse.jface.util.IPropertyChangeListener;

/**
 * Interface for the service "Manage Project", which allows the user to create, save and load Unagi Projects.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public interface ManageProjectsService {
	/**
	 * Creates a new Unagi Project, given some basic (mandatory) information about it.
	 * 
	 * @param name
	 *          Project name.
	 * @param folder
	 *          Folder in which the project is to be stored.
	 * 
	 * @return The newly created project.
	 * 
	 * @throws CouldNotSaveUnagiProjectException
	 *           If for some reason the new project could not be saved (e.g., I/O error on the specified folder).
	 */
	UnagiProject createNewProject(String name, File folder) throws CouldNotSaveUnagiProjectException;

	/**
	 * Saves an existing Unagi Project.
	 * 
	 * @param project
	 *          The project to be saved.
	 * 
	 * @throws CouldNotSaveUnagiProjectException
	 *           If for some reason the existing project could not be saved (e.g., I/O error on the project's folder).
	 */
	void saveProject(UnagiProject project) throws CouldNotSaveUnagiProjectException;

	/**
	 * Determines if an existing folder contains a Unagi Project.
	 * 
	 * @param folder
	 *          The file descriptor for the project's folder.
	 * 
	 * @return <code>true</code> if the folder contains a Unagi Project, <code>false</code> otherwise.
	 */
	boolean isProjectFolder(File folder);

	/**
	 * Loads a Unagi Project from the specified folder.
	 * 
	 * @param folder
	 *          The file descriptor for the project's folder.
	 * 
	 * @return The UnagiProject object representing the project that is stored in the specified folder.
	 * 
	 * @throws CouldNotLoadUnagiProjectException
	 *           If for some reason the project could not be loaded from the folder (e.g., I/O error on the specified
	 *           folder).
	 */
	UnagiProject loadProject(File folder) throws CouldNotLoadUnagiProjectException;

	/**
	 * Closes a Unagi Project.
	 * 
	 * @param project
	 *          The project to be closed.
	 */
	void closeProject(UnagiProject project);

	/**
	 * Returns the list of open projects.
	 * 
	 * @return A set object containing all projects that are currently open.
	 */
	Set<UnagiProject> getOpenProjects();

	/**
	 * Register a listener to property change events, which in the case of this service is a change in the list of open
	 * projects.
	 * 
	 * @param listener
	 *          The object that wants to listen to property change events.
	 */
	void addPropertyChangeListener(IPropertyChangeListener listener);

	/**
	 * Unregisters a listener to property change events, which in the case of this service is a change in the list of open
	 * projects.
	 * 
	 * @param listener
	 *          The object that doesn't want to listen to property change events anymore.
	 */
	void removePropertyChangeListener(IPropertyChangeListener listener);
}
