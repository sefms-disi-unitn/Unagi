package it.unitn.disi.unagi.application.exceptions;

/**
 * Class that represents all application exceptions. A type attribute indicates which kind of business logic exception
 * occurred.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class UnagiException extends Exception {
	/** Version UID for serialization purposes. */
	private static final long serialVersionUID = -7227343096875658813L;

	/** Type of the exception. Denotes the specific error that occurred. */
	private UnagiExceptionType type;

	/** Constructor. */
	public UnagiException(UnagiExceptionType type, Throwable cause) {
		super(cause);
		this.type = type;
	}

	/** Constructor. */
	public UnagiException(UnagiExceptionType type) {
		this.type = type;
	}

	/** Constructor. */
	public UnagiException(UnagiExceptionType type, String message, Throwable cause) {
		super(message, cause);
		this.type = type;
	}

	/** Constructor. */
	public UnagiException(UnagiExceptionType type, String message) {
		super(message);
		this.type = type;
	}

	/** Getter for type. */
	public UnagiExceptionType getType() {
		return type;
	}
}
