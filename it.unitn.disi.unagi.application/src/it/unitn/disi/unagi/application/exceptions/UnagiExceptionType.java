package it.unitn.disi.unagi.application.exceptions;

/**
 * Enumeration of types of business logic, application errors that can occur during the execution of Unagi. These types
 * are then used when creating instances of UnagiException.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 * @see it.unitn.disi.unagi.application.exceptions.UnagiException
 */
public enum UnagiExceptionType {
	/** A requirements model could not be compiled. */
	COULD_NOT_COMPILE_REQUIREMENTS_MODEL,
	
	/** The sub-directory that holds the models of a project could not be created. */
	COULD_NOT_CREATE_MODELS_SUBDIRECTORY,

	/** A requirements model file could not be created in a project. */
	COULD_NOT_CREATE_REQUIREMENTS_MODEL_FILE,

	/** The sub-directory that holds the Java sources of a project could not be created. */
	COULD_NOT_CREATE_SOURCES_SUBDIRECTORY,

	/** A requirements model file in a project could not be deleted. */
	COULD_NOT_DELETE_REQUIREMENTS_MODEL_FILE,

	/** The template for requirements model files could not be loaded from within the plug-in. */
	COULD_NOT_LOAD_REQUIREMENTS_MODEL_TEMPLATE_FILE,

	/** A Unagi project could not be loaded. */
	COULD_NOT_LOAD_UNAGI_PROJECT,

	/** A Unagi project could not be saved. */
	COULD_NOT_SAVE_UNAGI_PROJECT;
}
