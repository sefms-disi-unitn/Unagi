package it.unitn.disi.unagi.application.internal.services;

import it.unitn.disi.unagi.application.Activator;
import it.unitn.disi.unagi.application.exceptions.UnagiException;
import it.unitn.disi.unagi.application.exceptions.UnagiExceptionType;
import it.unitn.disi.unagi.application.nls.Messages;
import it.unitn.disi.unagi.application.services.ManageModelsService;
import it.unitn.disi.unagi.application.services.Unagi;
import it.unitn.disi.unagi.application.util.PluginLogger;
import it.unitn.disi.unagi.application.util.ResourceUtil;
import it.unitn.disi.unagi.domain.core.RequirementsModel;
import it.unitn.disi.unagi.domain.core.UnagiProject;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.osgi.framework.Bundle;

/**
 * Implementation of the "Manage Project" service.
 * 
 * @see it.unitn.disi.unagi.application.services.ManageProjectService
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class ManageModelsServiceBean implements ManageModelsService {
	/** The plug-in logger. */
	private static final PluginLogger logger = new PluginLogger(Activator.getInstance().getLog(), Activator.PLUGIN_ID);

	/** Extension for requirements model files. */
	private static final String MODEL_FILE_EXTENSION = "ecore"; //$NON-NLS-1$

	/** Path to the resources folder. */
	private static final String RESOURCES_FOLDER_PATH = "/resources/"; //$NON-NLS-1$ 

	/** Name of the goal model EMF file. */
	private static final String GOAL_MODEL_EMF_FILE_NAME = "goalmodel." + MODEL_FILE_EXTENSION; //$NON-NLS-1$ 

	/** Name of the LTL EMF file. */
	private static final String LTL_EMF_FILE_NAME = "LTL." + MODEL_FILE_EXTENSION; //$NON-NLS-1$ 

	/** The Unagi application. */
	private Unagi unagi;

	/** Constructor. */
	public ManageModelsServiceBean(Unagi unagi) {
		this.unagi = unagi;
	}

	/**
	 * @see it.unitn.disi.unagi.application.services.ManageModelsService#createNewRequirementsModel(it.unitn.disi.unagi.domain.core.UnagiProject,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public RequirementsModel createNewRequirementsModel(UnagiProject project, String name, String basePackage) throws UnagiException {
		logger.info("Creating new requirements model for project \"{0}\", with name \"{1}\" and base package \"{2}\"...", project.getName(), name, basePackage); //$NON-NLS-1$

		// Determine the name of the file that will store the requirements model and creates it.
		File modelsDir = checkModelsSubdirectory(project);
		String fileName = generateFileName(name);
		File file = new File(modelsDir, fileName);
		file = createRequirementsModelFile(file);

		// Creates the new model and associates it with the project.
		RequirementsModel model = new RequirementsModel(file, name);
		project.addRequirementsModel(model);
		model.setBasePackage(basePackage);

		// Creates the EMF model, linking with the Goal Model and LTL EMF files.
		createRequirementsEMFContents(model);

		// Saves the project.
		unagi.getManageProjectsService().saveProject(project);

		// Returns the newly created model.
		return model;
	}

	/** @see it.unitn.disi.unagi.application.services.ManageModelsService#deleteRequirementsModel(it.unitn.disi.unagi.domain.core.RequirementsModel) */
	@Override
	public void deleteRequirementsModel(RequirementsModel model) throws UnagiException {
		logger.info("Deleting requirements model \"{0}\" from project \"{1}\"", model.getName(), model.getProject().getName()); //$NON-NLS-1$

		// Deletes the file in the file system, if it exists. Reports any problems as an application exception.
		File file = model.getFile();
		if (file.exists() && file.isFile())
			if (!file.delete()) {
				logger.error("Could not delete requirements model file: {0}", file.getAbsolutePath()); //$NON-NLS-1$
				throw new UnagiException(UnagiExceptionType.COULD_NOT_DELETE_REQUIREMENTS_MODEL_FILE);
			}

		// Remove the model from the project and save the latter.
		UnagiProject project = model.getProject();
		project.removeRequirementsModel(model);
		unagi.getManageProjectsService().saveProject(project);
	}

	/**
	 * Generates the name of the file that will hold the model in disk automatically from the name of the model. This
	 * method basically converts spaces, underscores and dots to dashes and removes any non-alphanumeric characters
	 * (including accebnted letters).
	 * 
	 * @param name
	 *          The name of the model.
	 * @return A string with the name of the file that should represent the model in disk.
	 */
	private String generateFileName(String name) {
		// Generates the file name from the model name (in lower case).
		StringBuilder builder = new StringBuilder(name.toLowerCase());
		for (int i = 0; i < builder.length(); i++) {
			// Convert spaces/underscores/dots to dashes and removes any non alphanumeric characters (including accented
			// letters).
			char c = builder.charAt(i);
			if ((c == ' ') || (c == '_') || (c == '.'))
				builder.setCharAt(i, '-');
			else if (!(((c >= 'a') && (c <= 'z')) || ((c >= '0') && (c <= '9')) || (c == '-')))
				builder.deleteCharAt(i);
		}

		logger.info("Generated file name for model \"{0}\": {1}", name, builder.toString()); //$NON-NLS-1$
		return builder.append('.').append(MODEL_FILE_EXTENSION).toString();
	}

	/**
	 * Checks if the "models" sub-directory exists in the given project, creating it if it doesn't yet exist.
	 * 
	 * @param project
	 *          A Unagi project.
	 * @return A File object representing the "models" sub-directory of the Unagi project.
	 * @throws CouldNotCreateModelsSubdirectoryException
	 *           If for some reason the "models" sub-directory did not exist and could not be created in the Unagi
	 *           project's directory.
	 */
	private File checkModelsSubdirectory(UnagiProject project) throws UnagiException {
		// Check if the models sub-directory already exists.
		File modelsDir = new File(project.getFolder(), unagi.getProperty(Unagi.CFG_PROJECT_SUBDIR_MODELS));
		if (!modelsDir.isDirectory()) {
			logger.info("Models sub-directory \"{0}\" for project \"{1}\" doesn't exist. Creating now...", modelsDir, project.getName()); //$NON-NLS-1$

			// Doesn't exist. Create it, checking for problems.
			if (!modelsDir.mkdir()) {
				logger.error("Could not create models sub-directory: {0}", modelsDir.getAbsolutePath()); //$NON-NLS-1$
				throw new UnagiException(UnagiExceptionType.COULD_NOT_CREATE_MODELS_SUBDIRECTORY);
			}

			// Copies the Goal Model and LTL EMF files to the models subdirectory.
			Bundle bundle = Activator.getInstance().getBundle();
			try {
				File gmFile = new File(FileLocator.resolve(bundle.getEntry(RESOURCES_FOLDER_PATH + GOAL_MODEL_EMF_FILE_NAME)).toURI());
				File ltlFile = new File(FileLocator.resolve(bundle.getEntry(RESOURCES_FOLDER_PATH + LTL_EMF_FILE_NAME)).toURI());
				ResourceUtil.fileCopy(gmFile, new File(modelsDir, GOAL_MODEL_EMF_FILE_NAME));
				ResourceUtil.fileCopy(ltlFile, new File(modelsDir, LTL_EMF_FILE_NAME));
			}
			catch (Exception e) {
				logger.error(e, "Could not copy Goal Model and LTL EMF files to the models sub-directory: {0}", modelsDir.getAbsolutePath()); //$NON-NLS-1$
				throw new UnagiException(UnagiExceptionType.COULD_NOT_CREATE_MODELS_SUBDIRECTORY, e);
			}
		}
		return modelsDir;
	}

	/**
	 * Given a File object that represents where the requirements model's file should be created, checks if no file with
	 * the same name exists at the specified location (automatically renaming the new file's name if another file already
	 * exists) and creates a new file to represent the requirements model on disk.
	 * 
	 * @param file
	 *          The file descriptor that indicate where we want the requirements model file to be created.
	 * @return A File object that contains the file descriptor for the file that was actually created.
	 * @throws CouldNotCreateRequirementsModelFileException
	 *           If for some reason the file that represents the requirements model could not be created in the "models"
	 *           sub-directory of the Unagi project's directory.
	 */
	private File createRequirementsModelFile(File file) throws UnagiException {
		// Prepare variables to generate new file names if the file already exists.
		int count = 2;
		String originalName = file.getName();
		String ext = "." + MODEL_FILE_EXTENSION; //$NON-NLS-1$
		originalName = originalName.substring(0, originalName.indexOf(ext));

		// Checks that the file does not yet exist. If it does, rename it.
		while (file.exists() && file.isFile()) {
			file = new File(file.getParentFile(), originalName + '-' + (count++) + ext);
		}

		// Sure that the file doesn't exist, create the file.
		try {
			logger.info("Creating requirements model file \"{0}\"...", file); //$NON-NLS-1$
			file.createNewFile();
		}
		catch (IOException e) {
			logger.error(e, "Could not create requirements model file: {0}", file.getAbsolutePath()); //$NON-NLS-1$
			throw new UnagiException(UnagiExceptionType.COULD_NOT_CREATE_REQUIREMENTS_MODEL_FILE, e);
		}
		catch (SecurityException e) {
			logger.error(e, "Could not create requirements model file: {0}", file.getAbsolutePath()); //$NON-NLS-1$
			throw new UnagiException(UnagiExceptionType.COULD_NOT_CREATE_REQUIREMENTS_MODEL_FILE, e);
		}

		return file;
	}

	/**
	 * Given the requirements model object, fills its actual file (in the file system) with EMF contents to kickstart the
	 * creation of a requirements file. This method basically adds a main goal to the model, referring to the Goal class
	 * of the Goal Model EMF file, which should have been copied to the same folder as the requirements file when the
	 * project's models sub-directory was created.
	 * 
	 * @param model
	 *          The object that represents the requirements model.
	 */
	private void createRequirementsEMFContents(RequirementsModel model) throws UnagiException {
		logger.info("Creating EMF contents for model \"{0}\" (project \"{1}\") into file \"{2}\"...", model.getName(), model.getProject().getName(), model.getFile().getAbsolutePath()); //$NON-NLS-1$

		try {
			// Initializes the standalone factory implementation for ecore files and a new resource set.
			Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(MODEL_FILE_EXTENSION, new EcoreResourceFactoryImpl());
			ResourceSet resourceSet = new ResourceSetImpl();

			// Loads the goal model EMF file.
			File modelsSubDir = model.getFile().getParentFile();
			File gmFile = new File(modelsSubDir, GOAL_MODEL_EMF_FILE_NAME);
			final URI uri = URI.createFileURI(gmFile.getAbsolutePath());
			Resource ddlResource = resourceSet.createResource(uri);
			ddlResource.load(Collections.emptyMap());

			// Load the package from the goal model EMF file and create a new package for the requirements file.
			final EPackage ddlPackage = (EPackage) ddlResource.getContents().get(0);
			final EPackage newPackage = EcoreFactory.eINSTANCE.createEPackage();
			newPackage.setName(model.getName());
			newPackage.setNsPrefix(model.getBasePackage());
			newPackage.setNsURI(model.getFile().getName());

			// Create the main goal for the requirements file and adds it to its package.
			final EClass mainGoal = EcoreFactory.eINSTANCE.createEClass();
			mainGoal.setName(Messages.getString("application.services.manageModelsServiceBean.requirementsModelTemplate.mainGoal")); //$NON-NLS-1$
			newPackage.getEClassifiers().add(mainGoal);

			// Add the class Goal from the Goal Model EMF file as superclass of the main goal in the requirements model.
			final EClass superClass = (EClass) ddlPackage.getEClassifier("Goal"); //$NON-NLS-1$
			mainGoal.getESuperTypes().add(superClass);

			// Finally, create a new resource to save the requirements package into a new model file.
			Resource outputRes = resourceSet.createResource(URI.createFileURI(model.getFile().getAbsolutePath()));
			outputRes.getContents().add(newPackage);
			outputRes.save(Collections.emptyMap());
		}
		catch (Exception e) {
			logger.error(e, "Could not create EMF contents for requirements model file: {0}", model.getFile().getAbsolutePath()); //$NON-NLS-1$
			throw new UnagiException(UnagiExceptionType.COULD_NOT_CREATE_REQUIREMENTS_MODEL_FILE, e);
		}
	}
}
