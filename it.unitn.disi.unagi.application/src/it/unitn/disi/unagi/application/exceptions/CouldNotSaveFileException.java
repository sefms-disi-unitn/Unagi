package it.unitn.disi.unagi.application.exceptions;

import org.eclipse.core.resources.IFile;

/**
 * Exception class that represents an error occurred while a file was being saved.
 *
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class CouldNotSaveFileException extends UnagiException {
	/** File whose saving triggered the error. */
	protected IFile file;
	
	/** Constructor. */
	public CouldNotSaveFileException(IFile file) {
		this.file = file;
	}
	
	/** Constructor. */
	public CouldNotSaveFileException(IFile file, Throwable t) {
		super(t);
		this.file = file;
	}

	/** Getter for file. */
	public IFile getFile() {
		return file;
	}
}
