package it.unitn.disi.unagi.application.internal.services;

import it.unitn.disi.unagi.application.exceptions.CouldNotCreateModelsSubdirectoryException;
import it.unitn.disi.unagi.application.services.ManageModelsService;
import it.unitn.disi.unagi.application.services.Unagi;
import it.unitn.disi.unagi.domain.core.RequirementsModel;
import it.unitn.disi.unagi.domain.core.UnagiProject;

import java.io.File;

/**
 * Implementation of the "Manage Project" service.
 * 
 * @see it.unitn.disi.unagi.application.services.ManageProjectService
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public class ManageModelsServiceBean implements ManageModelsService {
	/** Extension for requirements model files. */
	private static final String REQUIREMENTS_MODEL_FILE_EXTENSION = ".ecore";

	/** The Unagi application. */
	private Unagi unagi;

	/** Constructor. */
	public ManageModelsServiceBean(Unagi unagi) {
		this.unagi = unagi;
	}
	
	/**
	 * @see it.unitn.disi.unagi.application.services.ManageModelsService#createNewRequirementsModel(it.unitn.disi.unagi.domain.core.UnagiProject,
	 *      java.lang.String)
	 */
	@Override
	public RequirementsModel createNewRequirementsModel(UnagiProject project, String name) throws CouldNotCreateModelsSubdirectoryException {
		// Determine the name of the file that will store the requirements model.
		File modelsDir = checkModelsSubdirectory(project);
		String fileName = generateFileName(name);
		File file = new File(modelsDir, fileName);

		// Creates the new model and associates it with the project.
		RequirementsModel model = new RequirementsModel(file, name);
		project.addRequirementsModel(model);

		// Returns the newly created model.
		return model;
	}

	/**
	 * TODO: document this method.
	 * 
	 * @param name
	 * @return
	 */
	private String generateFileName(String name) {
		// Generates the file name from the model name (in lowercase).
		StringBuilder builder = new StringBuilder(name.toLowerCase());
		for (int i = 0; i < builder.length(); i++) {
			// Convert spaces to dashes and removes any non alphanumeric characters (including accented letters).
			char c = builder.charAt(i);
			if (c == ' ')
				builder.setCharAt(i, '-');
			else if (!(((c >= 'a') && (c <= 'z')) || ((c >= '0') && (c <= '9'))))
				builder.deleteCharAt(i);
		}

		return builder.toString() + REQUIREMENTS_MODEL_FILE_EXTENSION;
	}

	/**
	 * TODO: document this method.
	 * 
	 * @param project
	 * @return
	 * @throws CouldNotCreateModelsSubdirectoryException
	 */
	private File checkModelsSubdirectory(UnagiProject project) throws CouldNotCreateModelsSubdirectoryException {
		// Check if the models sub-directory already exists.
		File modelsDir = new File(project.getFolder(), unagi.getProperty(Unagi.CFG_PROJECT_SUBDIR_MODELS));
		if (!modelsDir.isDirectory()) {
			// Doesn't exist. Create it, checking for problems.
			if (!modelsDir.mkdir())
				throw new CouldNotCreateModelsSubdirectoryException();
		}
		return null;
	}
}
