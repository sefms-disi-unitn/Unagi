package it.unitn.disi.unagi.application.services;

/**
 * Interface for the source files management service, which allows the user to create, read, write and delete source
 * files in a project.
 * 
 * Services contain application business logic that is GUI-independent and should be registered by the application
 * bundle in OSGi for proper dependency injection in the GUI classes provided by the main RCPApp bundle.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public interface IManageSourcesService extends IManageFilesService {
	/** File extension for source files. */
	String SOURCE_FILE_EXTENSION = "java"; //$NON-NLS-1$
}
