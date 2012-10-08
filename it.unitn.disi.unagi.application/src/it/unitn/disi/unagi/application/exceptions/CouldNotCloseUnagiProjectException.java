package it.unitn.disi.unagi.application.exceptions;

import org.eclipse.core.resources.IProject;

/**
 * Exception class that represents an error occurred while a project was being closed.
 *
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class CouldNotCloseUnagiProjectException extends UnagiException {
	/** Project whose closing triggered the error. */
	private IProject project;
	
	/** Constructor. */
	public CouldNotCloseUnagiProjectException(IProject project) {
		this.project = project;
	}
	
	/** Constructor. */
	public CouldNotCloseUnagiProjectException(IProject project, Throwable t) {
		super(t);
		this.project = project;
	}

	/** Getter for project. */
	public IProject getProject() {
		return project;
	}
}
