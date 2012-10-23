package it.unitn.disi.unagi.application.exceptions;

import org.eclipse.core.resources.IFile;

/**
 * Exception class that represents an error occurred while a file was being deleted.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class CouldNotDeleteFileException extends UnagiException {
	/** File whose deletion triggered the error. */
	private IFile file;

	/** Constructor. */
	public CouldNotDeleteFileException(IFile file) {
		this.file = file;
	}

	/** Constructor. */
	public CouldNotDeleteFileException(IFile requirementsModel, Throwable t) {
		super(t);
		this.file = requirementsModel;
	}

	/** Getter for requirementsModel. */
	public IFile getRequirementsModel() {
		return file;
	}
}
