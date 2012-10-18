package it.unitn.disi.unagi.application.services;

import it.unitn.disi.unagi.application.exceptions.CouldNotReadFileException;
import it.unitn.disi.unagi.application.exceptions.CouldNotSaveFileException;

import org.eclipse.core.resources.IFile;

/**
 * Interface for generic files management service, publishing a common API for any class that manipulates some type of
 * file in a project. This interface allows the GUI elements to treat any file management service in an uniform way
 * (i.e., to use polymorphism).
 * 
 * Services contain application business logic that is GUI-independent and should be registered by the application
 * bundle in OSGi for proper dependency injection in the GUI classes provided by the main RCPApp bundle.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public interface IManageFilesService {
	/** Key value to be associated with file descriptors when they are opened by the GUI. */
	String FILE_KEY = "unagi.managed.file"; //$NON-NLS-1$

	/**
	 * Reads the contents of the given file.
	 * 
	 * @param file
	 *          The descriptor of the file whose contents should be read.
	 * @return A string buffer with the file's contents.
	 * @throws CouldNotReadFileException
	 *           If there are any problems in reading the file.
	 */
	StringBuffer readFile(IFile file) throws CouldNotReadFileException;

	/**
	 * Saves a given content to the specified file.
	 * 
	 * @param file
	 *          The descriptor of the file in which the contents should be saved.
	 * @param contents
	 *          The contents that should be saved in the file.
	 * @throws CouldNotSaveFileException
	 *           If there are any problems in saving the contents in the file.
	 */
	void saveFile(IFile file, String contents) throws CouldNotSaveFileException;
}
