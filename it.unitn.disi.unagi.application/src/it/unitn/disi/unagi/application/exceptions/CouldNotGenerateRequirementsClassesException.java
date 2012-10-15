package it.unitn.disi.unagi.application.exceptions;

import org.eclipse.core.resources.IFile;

/**
 * Exception class that represents an error occurred while classes were being generated for a requirement model.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class CouldNotGenerateRequirementsClassesException extends UnagiException {
	/** Requirements model whose class generation produced the error. */
	private IFile requirementsModel;

	/** Constructor. */
	public CouldNotGenerateRequirementsClassesException(IFile requirementsModel) {
		this.requirementsModel = requirementsModel;
	}

	/** Constructor. */
	public CouldNotGenerateRequirementsClassesException(IFile requirementsModel, Throwable t) {
		super(t);
		this.requirementsModel = requirementsModel;
	}

	/** Getter for requirementsModel. */
	public IFile getRequirementsModel() {
		return requirementsModel;
	}
}
