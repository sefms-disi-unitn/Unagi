package it.unitn.disi.unagi.application.services;

import it.unitn.disi.unagi.application.exceptions.CouldNotReadFileException;
import it.unitn.disi.unagi.application.exceptions.CouldNotSaveFileException;

import org.eclipse.core.resources.IFile;

/**
 * TODO: document this type.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public interface IManageFilesService {
	/** TODO: document this field. */
	String FILE_KEY = "unagi.managed.file"; //$NON-NLS-1$

	/**
	 * TODO: document this method.
	 * 
	 * @param file
	 * @return
	 */
	StringBuffer readFile(IFile file) throws CouldNotReadFileException;

	/**
	 * TODO: document this method.
	 * 
	 * @param file
	 * @param contents
	 */
	void saveFile(IFile file, String contents) throws CouldNotSaveFileException;
}
