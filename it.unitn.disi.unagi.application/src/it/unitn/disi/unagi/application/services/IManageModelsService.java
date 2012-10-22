package it.unitn.disi.unagi.application.services;

import it.unitn.disi.unagi.application.exceptions.CouldNotCompileConstraintsFileException;
import it.unitn.disi.unagi.application.exceptions.CouldNotCreateFileException;
import it.unitn.disi.unagi.application.exceptions.CouldNotDeleteFileException;
import it.unitn.disi.unagi.application.exceptions.CouldNotGenerateRequirementsClassesException;
import it.unitn.disi.unagi.application.nls.Messages;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;

/**
 * Interface for the model management service, which allows the user to perform operations, such as creation, deletion,
 * compilation, etc., on models such as requirements models or constraints files.
 * 
 * Services contain application business logic that is GUI-independent and should be registered by the application
 * bundle in OSGi for proper dependency injection in the GUI classes provided by the main RCPApp bundle.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public interface IManageModelsService extends IManageFilesService {
	/** File extension for requirements models. */
	String REQUIREMENTS_MODEL_EXTENSION = "ecore"; //$NON-NLS-1$

	/** File extension for constraint files. */
	String CONSTRAINTS_FILE_EXTENSION = "ocl"; //$NON-NLS-1$

	/** Path for the file that defines base classes for goal model elements. */
	IPath GORE_EMF_FILE_PATH = new Path("META-INF/gore.ecore"); //$NON-NLS-1$

	/** Path for the file that defines base classes for goal model elements. */
	IPath ECA_EMF_FILE_PATH = new Path("META-INF/eca.ecore"); //$NON-NLS-1$

	/** Path for the file that defines base classes for goal model elements. */
	IPath LTL_EMF_FILE_PATH = new Path("META-INF/ltl.ecore"); //$NON-NLS-1$

	/** Path for the generator file for the base packages eca, goalmodel and LTL. */
	IPath BASE_GENMODEL_FILE_PATH = new Path("META-INF/zanshin.genmodel"); //$NON-NLS-1$

	/** Path for the template for new constraints files. */
	IPath CONSTRAINTS_TEMPLATE_FILE_PATH = new Path("META-INF/template.ocl"); //$NON-NLS-1$;

	/** Name of the main goal used when creating a new requirements model. */
	String MAIN_GOAL_BASE_NAME = Messages.getString("emf.mainGoal.baseName"); //$NON-NLS-1$

	/** Name of the variable that represents the package name in the constraints template file. */
	String CONSTRAINTS_VARIABLE_PACKAGE_NAME = "packageName"; //$NON-NLS-1$

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
	 * @throws CouldNotCreateFileException
	 *           If there are any problems in the creation of the model.
	 */
	IFile createNewRequirementsModel(IProgressMonitor progressMonitor, IProject project, String name, String basePackage) throws CouldNotCreateFileException;

	/**
	 * Deletes a requirements model from a project, also deleting it from the file system.
	 * 
	 * @param progressMonitor
	 *          The workbench's progress monitor, in case the operation takes a long time.
	 * @param modelFile
	 *          The file representing the requirements model to be deleted.
	 * @throws CouldNotDeleteFileException
	 *           If there are any problems in the deletion of the model.
	 */
	void deleteRequirementsModel(IProgressMonitor progressMonitor, IFile modelFile) throws CouldNotDeleteFileException;

	/**
	 * Generates source files for the classes that are defined in a given requirements model.
	 * 
	 * @param progressMonitor
	 *          The workbench's progress monitor, in case the operation takes a long time.
	 * @param modelFile
	 *          The file representing the requirements model whose classes should be generated.
	 * @return The folder in which the classes were generated.
	 * @throws CouldNotGenerateRequirementsClassesException
	 *           If there are any problems in the generation of source files.
	 */
	IFolder generateRequirementsClasses(IProgressMonitor progressMonitor, IFile modelFile) throws CouldNotGenerateRequirementsClassesException;

	/**
	 * Creates a new constraints file in the given project.
	 * 
	 * @param progressMonitor
	 *          The workbench's progress monitor, in case the operation takes a long time.
	 * @param project
	 *          The project in which the file should be created.
	 * @param name
	 *          The name of the new constraints file.
	 * @return The newly created constraints file.
	 * @throws CouldNotCreateFileException
	 *           If there are any problems in the creation of the file.
	 */
	IFile createNewConstraintsFile(IProgressMonitor progressMonitor, IProject project, String name) throws CouldNotCreateFileException;

	/**
	 * Deletes a constraints file from a project, also deleting it from the file system.
	 * 
	 * @param progressMonitor
	 *          The workbench's progress monitor, in case the operation takes a long time.
	 * @param constraintsFile
	 *          The constraints file to be deleted.
	 * @throws CouldNotDeleteFileException
	 *           If there are any problems in the deletion of the file.
	 */
	void deleteConstraintsFile(IProgressMonitor progressMonitor, IFile constraintsFile) throws CouldNotDeleteFileException;
	
	/**
	 * TODO: document this method.
	 * @param progressMonitor
	 * @param constraintsFile
	 * @return
	 * @throws CouldNotCompileConstraintsFileException
	 */
	IFile compileConstraintsFile(IProgressMonitor progressMonitor, IFile constraintsFile) throws CouldNotCompileConstraintsFileException;
}
