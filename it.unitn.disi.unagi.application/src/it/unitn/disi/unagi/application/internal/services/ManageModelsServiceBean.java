package it.unitn.disi.unagi.application.internal.services;

import it.unitn.disi.unagi.application.Activator;
import it.unitn.disi.unagi.application.exceptions.CouldNotCreateModelsSubdirectoryException;
import it.unitn.disi.unagi.application.exceptions.CouldNotCreateRequirementsModelFileException;
import it.unitn.disi.unagi.application.exceptions.CouldNotDeleteRequirementsModelFileException;
import it.unitn.disi.unagi.application.exceptions.CouldNotSaveUnagiProjectException;
import it.unitn.disi.unagi.application.services.ManageModelsService;
import it.unitn.disi.unagi.application.services.Unagi;
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
	/** Extension for requirements model files. */
	private static final String REQUIREMENTS_MODEL_FILE_EXTENSION = ".ecore";

	/** Requirements model template. Statically loaded from /resources/template.${REQUIREMENTS_MODEL_FILE_EXTENSION}. */
	private static final String REQUIREMENTS_MODEL_TEMPLATE;
	static {
		Bundle bundle = Activator.getContext().getBundle();
		StringBuilder builder = new StringBuilder();
		try {
			URL url = FileLocator.resolve(bundle.getEntry("/resources/template" + REQUIREMENTS_MODEL_FILE_EXTENSION));
			File templateFile = new File(url.toURI());
			Scanner scanner = new Scanner(templateFile);
			while (scanner.hasNextLine())
				builder.append(scanner.nextLine()).append('\n');
		}
		catch (Exception e) {
			// TODO: exception handling.
			e.printStackTrace();
		}
		finally {
			REQUIREMENTS_MODEL_TEMPLATE = builder.toString();
		}
	}

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
	public RequirementsModel createNewRequirementsModel(UnagiProject project, String name, String basePackage) throws CouldNotCreateModelsSubdirectoryException, CouldNotCreateRequirementsModelFileException, CouldNotSaveUnagiProjectException {
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
		try {
			String template = REQUIREMENTS_MODEL_TEMPLATE.replace("${name}", model.getName()).replace("${filename}", file.getName()).replace("${base-package}", model.getBasePackage());
			PrintWriter out = new PrintWriter(file);
			out.println(template);
			out.close();
		}
		catch (FileNotFoundException e) {
			throw new CouldNotCreateRequirementsModelFileException(e);
		}

		// Returns the newly created model.
		return model;
	}

	/** @see it.unitn.disi.unagi.application.services.ManageModelsService#deleteRequirementsModel(it.unitn.disi.unagi.domain.core.RequirementsModel) */
	@Override
	public void deleteRequirementsModel(RequirementsModel model) throws CouldNotDeleteRequirementsModelFileException, CouldNotSaveUnagiProjectException {
		// Deletes the file in the file system, if it exists. Reports any problems as an application exception.
		File file = model.getFile();
		if (file.exists() && file.isFile())
			if (!file.delete())
				throw new CouldNotDeleteRequirementsModelFileException();

		// Remove the model from the project and save the latter.
		UnagiProject project = model.getProject();
		project.removeRequirementsModel(model);
		unagi.getManageProjectsService().saveProject(project);
	}

	/**
	 * TODO: document this method.
	 * 
	 * @param name
	 * @return
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
		return modelsDir;
	}

	/**
	 * TODO: document this method.
	 * 
	 * @param file
	 * @return
	 * @throws CouldNotCreateRequirementsModelFileException
	 */
	private File createRequirementsModelFile(File file) throws CouldNotCreateRequirementsModelFileException {
		// Prepare variables to generate new file names if the file already exists.
		int count = 2;
		String originalName = file.getName();
		originalName = originalName.substring(0, originalName.indexOf(REQUIREMENTS_MODEL_FILE_EXTENSION));

		// Checks that the file does not yet exist. If it does, rename it.
		while (file.exists() && file.isFile()) {
			file = new File(file.getParentFile(), originalName + "-" + (count++) + REQUIREMENTS_MODEL_FILE_EXTENSION);
		}

		// Sure that the file doesn't exist, create the file.
		try {
			file.createNewFile();
		}
		catch (IOException e) {
			throw new CouldNotCreateRequirementsModelFileException(e);
		}
		catch (SecurityException e) {
			throw new CouldNotCreateRequirementsModelFileException(e);
		}

		return file;
	}
}
