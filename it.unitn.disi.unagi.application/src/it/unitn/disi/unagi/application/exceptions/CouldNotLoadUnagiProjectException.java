package it.unitn.disi.unagi.application.exceptions;

/**
 * Exception representing a failure during a Unagi Project loading.
 *
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class CouldNotLoadUnagiProjectException extends UnagiException {
	/** Version UID for serialization purposes. */
	private static final long serialVersionUID = -5040791811464016132L;

	/** Constructor. */
	public CouldNotLoadUnagiProjectException(Throwable cause) {
		super(cause);
	}
}
