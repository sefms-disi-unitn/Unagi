package it.unitn.disi.unagi.application.exceptions;

import org.eclipse.core.resources.IFile;

/**
 * Exception class that represents an error occurred while a requirements model was being deleted.
 *
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class CouldNotDeleteRequirementsModelException extends UnagiException {
	/** Project whose requirements model deletion triggered the error. */
	private IFile requirementsModel;
	
	/** Constructor. */
	public CouldNotDeleteRequirementsModelException(IFile requirementsModel) {
		this.requirementsModel = requirementsModel;
	}
	
	/** Constructor. */
	public CouldNotDeleteRequirementsModelException(IFile requirementsModel, Throwable t) {
		super(t);
		this.requirementsModel = requirementsModel;
	}

	/** Getter for requirementsModel. */
	public IFile getRequirementsModel() {
		return requirementsModel;
	}
}
