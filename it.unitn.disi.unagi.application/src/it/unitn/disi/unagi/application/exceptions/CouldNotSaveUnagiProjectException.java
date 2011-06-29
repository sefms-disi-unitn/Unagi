package it.unitn.disi.unagi.application.exceptions;

/**
 * Exception representing a failure during a Unagi Project saving.
 *
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class CouldNotSaveUnagiProjectException extends Exception {
	/** Version UID for serialization purposes. */
	private static final long serialVersionUID = -448112751502339551L;

	/** Constructor. */
	public CouldNotSaveUnagiProjectException(Throwable cause) {
		super(cause);
	}
}
