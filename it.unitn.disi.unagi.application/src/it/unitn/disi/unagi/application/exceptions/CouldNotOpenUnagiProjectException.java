package it.unitn.disi.unagi.application.exceptions;

import org.eclipse.core.resources.IProject;

/**
 * Exception class that represents an error occurred while a project was being opened.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class CouldNotOpenUnagiProjectException extends UnagiException {
	/** Project whose opening triggered the error. */
	private IProject project;

	/** Constructor. */
	public CouldNotOpenUnagiProjectException(IProject project) {
		this.project = project;
	}

	/** Constructor. */
	public CouldNotOpenUnagiProjectException(IProject project, Throwable t) {
		super(t);
		this.project = project;
	}

	/** Getter for project. */
	public IProject getProject() {
		return project;
	}
}
