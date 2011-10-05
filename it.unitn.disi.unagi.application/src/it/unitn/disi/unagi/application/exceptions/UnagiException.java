package it.unitn.disi.unagi.application.exceptions;

/**
 * Abstract class that serves as superclass for all application exceptions.
 *
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public abstract class UnagiException extends Exception {
	/** Version UID for serialization purposes. */
	private static final long serialVersionUID = -7227343096875658813L;

	/** Constructor. */
	public UnagiException(Throwable cause) {
		super(cause);
	}

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
}
