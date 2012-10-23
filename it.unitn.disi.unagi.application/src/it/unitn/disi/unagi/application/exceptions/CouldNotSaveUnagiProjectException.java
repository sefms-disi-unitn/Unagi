package it.unitn.disi.unagi.application.exceptions;

import org.eclipse.core.resources.IProject;

/**
 * Exception class that represents an error occurred while a project was being created.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class CouldNotSaveUnagiProjectException extends UnagiException {
	/** Project whose creation triggered the error. */
	private IProject project;

	/** Constructor. */
	public CouldNotSaveUnagiProjectException(IProject project) {
		this.project = project;
	}

	/** Constructor. */
	public CouldNotSaveUnagiProjectException(IProject project, Throwable t) {
		super(t);
		this.project = project;
	}

	/** Getter for project. */
	public IProject getProject() {
		return project;
	}
}
