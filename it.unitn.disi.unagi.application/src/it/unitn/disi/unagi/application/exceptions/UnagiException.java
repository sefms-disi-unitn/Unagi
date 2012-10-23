package it.unitn.disi.unagi.application.exceptions;

/**
 * Superclass of all exceptions that are application-specific for Unagi.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class UnagiException extends Exception {
	/** Constructor. */
	public UnagiException() {
		super();
	}

	/** Constructor. */
	public UnagiException(String message, Throwable cause) {
		super(message, cause);
	}

	/** Constructor. */
	public UnagiException(String message) {
		super(message);
	}

	/** Constructor. */
	public UnagiException(Throwable cause) {
		super(cause);
	}
}
