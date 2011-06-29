package it.unitn.disi.unagi.application.persistence;

import it.unitn.disi.unagi.domain.core.UnagiProject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Interface common to any persistence manager implementation for objects of the class UnagiProject.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public interface UnagiProjectPersistenceManager {
	/**
	 * Saves the contents of the Unagi Project to a file, overwriting its previous contents in the case of an existing
	 * file.
	 * 
	 * @param project
	 *          The Unagi Project to save.
	 * @param folder
	 *          The folder in which the project should be saved.
	 */
	void save(UnagiProject project, File folder) throws IOException;

	/**
	 * Checks the folder to determine if contains a Unagi Project.
	 * 
	 * @param folder
	 *          The folder to be checked.
	 * @return <code>true</code> if the folder contains a Unagi Project, <code>false</code> otherwise.
	 */
	boolean checkProjectFolder(File folder);

	/**
	 * Loads the contents of an Unagi Project from a file.
	 * 
	 * @param folder
	 *          Folder from which the contents of the Unagi Project will be loaded.
	 * 
	 * @return A UnagiProject object with the contents read from the file.
	 * 
	 * @throws ClassNotFoundException
	 *           If the class UnagiProject is not found at the classpath when being deserialized.
	 * @throws FileNotFoundException
	 *           If the specified file is not found.
	 * @throws IOException
	 *           If any I/O errors occur while reading the file.
	 */
	UnagiProject load(File folder) throws ClassNotFoundException, FileNotFoundException, IOException;
}
