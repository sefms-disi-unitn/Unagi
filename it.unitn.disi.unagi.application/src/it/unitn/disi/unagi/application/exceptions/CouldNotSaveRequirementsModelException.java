package it.unitn.disi.unagi.application.exceptions;

import org.eclipse.core.resources.IProject;

/**
 * Exception class that represents an error occurred while a requirements model was being created.
 *
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class CouldNotSaveRequirementsModelException extends UnagiException {
	/** Project whose requirements model creation triggered the error. */
	private IProject project;
	
	/** Constructor. */
	public CouldNotSaveRequirementsModelException(IProject project) {
		this.project = project;
	}
	
	/** Constructor. */
	public CouldNotSaveRequirementsModelException(IProject project, Throwable t) {
		super(t);
		this.project = project;
	}

	/** Getter for project. */
	public IProject getProject() {
		return project;
	}
}
