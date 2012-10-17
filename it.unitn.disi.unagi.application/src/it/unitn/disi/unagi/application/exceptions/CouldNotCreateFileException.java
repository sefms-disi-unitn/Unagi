package it.unitn.disi.unagi.application.exceptions;

import org.eclipse.core.resources.IFile;

/**
 * Exception class that represents an error occurred while a file was being created.
 *
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class CouldNotCreateFileException extends UnagiException {
	/** File whose creation triggered the error. */
	private IFile file;
	
	/** Constructor. */
	public CouldNotCreateFileException(IFile file) {
		this.file = file;
	}
	
	/** Constructor. */
	public CouldNotCreateFileException(IFile file, Throwable t) {
		super(t);
		this.file = file;
	}

	/** Getter for file. */
	public IFile getFile() {
		return file;
	}
}
