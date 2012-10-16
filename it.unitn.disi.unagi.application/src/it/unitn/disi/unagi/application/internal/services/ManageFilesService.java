package it.unitn.disi.unagi.application.internal.services;

import it.unitn.disi.unagi.application.exceptions.CouldNotReadFileException;
import it.unitn.disi.unagi.application.exceptions.CouldNotSaveFileException;
import it.unitn.disi.unagi.application.services.IManageFilesService;
import it.unitn.disi.util.io.FileIOUtil;

import java.io.IOException;

import org.eclipse.core.resources.IFile;

/**
 * TODO: document this type.
 *
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public abstract class ManageFilesService implements IManageFilesService {
	/** @see it.unitn.disi.unagi.application.services.IManageFilesService#readFile(org.eclipse.core.resources.IFile) */
	@Override
	public StringBuffer readFile(IFile file) throws CouldNotReadFileException {
		// Tries to read the contents of the file from its location.
		try {
			return FileIOUtil.readFile(file.getLocation().toString());
		}
		
		// In case of I/O errors, throws an application exception.
		catch (IOException e) {
			throw new CouldNotReadFileException(file, e);
		}
	}

	/** @see it.unitn.disi.unagi.application.services.IManageFilesService#saveFile(org.eclipse.core.resources.IFile, java.lang.String) */
	@Override
	public void saveFile(IFile file, String contents) throws CouldNotSaveFileException {
		// Tries to save the contents to the file at its location.
		try {
			FileIOUtil.saveFile(file.getLocation().toString(), contents);
		}
		
		// In case of I/O errors, throws an application exception.
		catch (IOException e) {
			throw new CouldNotSaveFileException(file, e);
		}
	}
}
