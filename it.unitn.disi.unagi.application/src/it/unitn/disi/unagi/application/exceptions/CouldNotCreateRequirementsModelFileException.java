package it.unitn.disi.unagi.application.exceptions;

/**
 * Exception representing a failure during a Unagi Project saving.
 *
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class CouldNotCreateRequirementsModelFileException extends UnagiException {
	/** Version UID for serialization purposes. */
	private static final long serialVersionUID = 4318373500005400992L;

	/** Constructor. */
	public CouldNotCreateRequirementsModelFileException(Throwable cause) {
		super(cause);
	}
}
