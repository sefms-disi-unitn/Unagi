package it.unitn.disi.unagi.application.services;

import it.unitn.disi.unagi.application.exceptions.CouldNotDeleteRequirementsModelException;
import it.unitn.disi.unagi.application.exceptions.CouldNotGenerateRequirementsClassesException;
import it.unitn.disi.unagi.application.exceptions.CouldNotSaveRequirementsModelException;
import it.unitn.disi.unagi.application.nls.Messages;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;

/**
 * Interface for the model management service, which allows the user to perform operations, such as creation, deletion,
 * compilation, etc., on models such as requirements models.
 * 
 * Services contain application business logic that is GUI-independent and should be registered by the application
 * bundle in OSGi for proper dependency injection in the GUI classes provided by the main RCPApp bundle.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public interface IManageModelsService {
	/** File extension for requirements models. */
	String REQUIREMENTS_MODEL_EXTENSION = "ecore"; //$NON-NLS-1$
	
	/** File extension for Java source files. */
	String JAVA_SOURCE_EXTENSION = "java"; //$NON-NLS-1$

	/** Path for the ECore file that defines base classes for goal model elements. */
	IPath GORE_EMF_FILE_PATH = new Path("META-INF/gore.ecore"); //$NON-NLS-1$
	
	/** Path for the GenModel file for the base packages eca, goalmodel and LTL. */
	IPath BASE_GENMODEL_FILE_PATH = new Path("META-INF/zanshin.genmodel"); //$NON-NLS-1$

	/** Name of the main goal used when creating a new requirements model. */
	String MAIN_GOAL_BASE_NAME = Messages.getString("emf.mainGoal.baseName"); //$NON-NLS-1$

	/**
	 * Creates a new requirements model in the given project.
	 * 
	 * @param progressMonitor
	 *          The workbench's progress monitor, in case the operation takes a long time.
	 * @param project
	 *          The project in which the requirements model should be created.
	 * @param name
	 *          The name of the new requirements model.
	 * @param basePackage
	 *          The base package for the new requirements model.
	 * @return The file that represents the newly created requirements model.
	 * @throws CouldNotSaveRequirementsModelException
	 *           If there are any problems in the creation of the model.
	 */
	IFile createNewRequirementsModel(IProgressMonitor progressMonitor, IProject project, String name, String basePackage) throws CouldNotSaveRequirementsModelException;

	/**
	 * Deletes a requirements model from a project, also deleting it from the file system.
	 * 
	 * @param progressMonitor
	 *          The workbench's progress monitor, in case the operation takes a long time.
	 * @param modelFile
	 *          The file representing the requirements model to be deleted.
	 * @throws CouldNotDeleteRequirementsModelException
	 *           If there are any problems in the deletion of the model.
	 */
	void deleteRequirementsModel(IProgressMonitor progressMonitor, IFile modelFile) throws CouldNotDeleteRequirementsModelException;

	/**
	 * TODO: document this method.
	 * 
	 * @param progressMonitor
	 * @param modelFile
	 * @return
	 * @throws CouldNotGenerateRequirementsClassesException
	 */
	IFolder generateRequirementsClasses(IProgressMonitor progressMonitor, IFile modelFile) throws CouldNotGenerateRequirementsClassesException;
}
