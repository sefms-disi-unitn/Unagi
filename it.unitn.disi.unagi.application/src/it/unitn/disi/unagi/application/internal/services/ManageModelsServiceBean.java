package it.unitn.disi.unagi.application.internal.services;

import it.unitn.disi.unagi.application.Activator;
import it.unitn.disi.unagi.application.exceptions.UnagiException;
import it.unitn.disi.unagi.application.exceptions.UnagiExceptionType;
import it.unitn.disi.unagi.application.services.ManageModelsService;
import it.unitn.disi.unagi.application.services.Unagi;
import it.unitn.disi.unagi.application.util.PluginLogger;
import it.unitn.disi.unagi.domain.core.RequirementsModel;
import it.unitn.disi.unagi.domain.core.UnagiProject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Scanner;

import org.eclipse.core.runtime.FileLocator;
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
	private static final String REQUIREMENTS_MODEL_FILE_EXTENSION = ".ecore"; //$NON-NLS-1$

	/** Path to the template for model files. */
	private static final String REQUIREMENTS_MODEL_TEMPLATE_PATH = "/resources/template" + REQUIREMENTS_MODEL_FILE_EXTENSION; //$NON-NLS-1$ 

	/** Requirements model template. */
	private static String REQUIREMENTS_MODEL_TEMPLATE;

	/** The Unagi application. */
	private Unagi unagi;

	/** Constructor. */
	public ManageModelsServiceBean(Unagi unagi) throws UnagiException {
		this.unagi = unagi;
		init();
	}

	/**
	 * Initializes the singleton instance of this application class.
	 * 
	 * @throws UnagiException
	 *           If, for some reason, the requirements model template could not be loaded from within the plug-in.
	 */
	private void init() throws UnagiException {
		logger.info("Initializing the \"Manage Models\" service..."); //$NON-NLS-1$
		logger.info("Loading contents from requirements model template: {0} ...", REQUIREMENTS_MODEL_TEMPLATE_PATH); //$NON-NLS-1$

		// Uses the plug-in's execution bundle to locate a file from within the plug-in.
		Bundle bundle = Activator.getInstance().getBundle();
		StringBuilder builder = new StringBuilder();
		try {
			URL url = FileLocator.resolve(bundle.getEntry(REQUIREMENTS_MODEL_TEMPLATE_PATH));
			File templateFile = new File(url.toURI());

			// Reads the file line by line, adding them to the string builder.
			Scanner scanner = new Scanner(templateFile);
			while (scanner.hasNextLine())
				builder.append(scanner.nextLine()).append('\n');
			scanner.close();
		}
		catch (Exception e) {
			logger.error(e, "Could not load contents from requirements model template"); //$NON-NLS-1$
			throw new UnagiException(UnagiExceptionType.COULD_NOT_LOAD_REQUIREMENTS_MODEL_TEMPLATE_FILE, e);
		}
		finally {
			// Stores the contents of the template in the static property for later use.
			REQUIREMENTS_MODEL_TEMPLATE = builder.toString();
		}
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

		// Saves the project.
		unagi.getManageProjectsService().saveProject(project);

		// Fill in the new model file with the template, replacing the values for name, filename and base package.
		PrintWriter out = null;
		try {
			String template = REQUIREMENTS_MODEL_TEMPLATE.replace("${name}", model.getName()).replace("${filename}", file.getName()).replace("${base-package}", model.getBasePackage()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			out = new PrintWriter(file);
			out.println(template);
		}
		catch (FileNotFoundException e) {
			logger.error(e, "Could not create requirements model file"); //$NON-NLS-1$
			throw new UnagiException(UnagiExceptionType.COULD_NOT_CREATE_REQUIREMENTS_MODEL_FILE, e);
		}
		finally {
			if (out != null)
				out.close();
		}

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
		return builder.toString() + REQUIREMENTS_MODEL_FILE_EXTENSION;
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
		originalName = originalName.substring(0, originalName.indexOf(REQUIREMENTS_MODEL_FILE_EXTENSION));

		// Checks that the file does not yet exist. If it does, rename it.
		while (file.exists() && file.isFile()) {
			file = new File(file.getParentFile(), originalName + '-' + (count++) + REQUIREMENTS_MODEL_FILE_EXTENSION);
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
}
