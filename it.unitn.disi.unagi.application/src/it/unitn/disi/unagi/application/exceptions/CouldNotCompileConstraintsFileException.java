package it.unitn.disi.unagi.application.exceptions;

import org.eclipse.core.resources.IFile;

/**
 * Exception class that represents an error occurred while constraints were being compiled into rules.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class CouldNotCompileConstraintsFileException extends UnagiException {
	/** Constraints file whose compilation produced the error. */
	private IFile constraintsFile;

	/** Constructor. */
	public CouldNotCompileConstraintsFileException(IFile constraintsFile) {
		this.constraintsFile = constraintsFile;
	}

	/** Constructor. */
	public CouldNotCompileConstraintsFileException(IFile constraintsFile, Throwable cause) {
		super(cause);
		this.constraintsFile = constraintsFile;
	}

	/** Getter for constraintsFile. */
	public IFile getConstraintsFile() {
		return constraintsFile;
	}
}
