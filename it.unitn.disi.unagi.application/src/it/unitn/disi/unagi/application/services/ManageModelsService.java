package it.unitn.disi.unagi.application.services;

import it.unitn.disi.unagi.application.exceptions.CouldNotCreateModelsSubdirectoryException;
import it.unitn.disi.unagi.application.exceptions.CouldNotCreateRequirementsModelFileException;
import it.unitn.disi.unagi.application.exceptions.CouldNotDeleteRequirementsModelFileException;
import it.unitn.disi.unagi.application.exceptions.CouldNotSaveUnagiProjectException;
import it.unitn.disi.unagi.domain.core.RequirementsModel;
import it.unitn.disi.unagi.domain.core.UnagiProject;

/**
 * Interface for the service "Manage Models", which allows the user to manipulate (create, delete, etc.) models in a
 * Unagi project.
 * 
 * @author Vitor E. Silva Souza (vitorsouza@gmail.com)
 * @version 1.0
 */
public interface ManageModelsService {
	/**
	 * Creates a new requirements model in the given Unagi project.
	 * 
	 * @param project
	 *          The Unagi project that will hold the requirements model.
	 * @param name
	 *          The name of the requirements model.
	 * @param basePackage
	 *          The base package for classes generated from this requirements model.
	 * @return A RequirementsModel instance, which is a domain class that represents requirements models.
	 * @throws CouldNotCreateModelsSubdirectoryException
	 *           If for some reason the "models" sub-directory did not exist and could not be created in the Unagi
	 *           project's directory.
	 * @throws CouldNotCreateRequirementsModelFileException
	 *           If for some reason the file that represents the requirements model could not be created in the "models"
	 *           sub-directory of the Unagi project's directory.
	 * @throws CouldNotSaveUnagiProjectException
	 *           If for some reason the new project could not be saved.
	 */
	RequirementsModel createNewRequirementsModel(UnagiProject project, String name, String basePackage) throws CouldNotCreateModelsSubdirectoryException, CouldNotCreateRequirementsModelFileException, CouldNotSaveUnagiProjectException;

	/**
	 * Deletes the specified requirements model, removing it from its parent Unagi project.
	 * 
	 * @param model
	 * @throws CouldNotDeleteRequirementsModelFileException
	 *           If for some reason the file that represents the requirements model in the "models" sub-directory of the
	 *           Unagi project's directory cannot be deleted.
	 * @throws CouldNotSaveUnagiProjectException
	 *           If for some reason the new project could not be saved.
	 */
	void deleteRequirementsModel(RequirementsModel model) throws CouldNotDeleteRequirementsModelFileException, CouldNotSaveUnagiProjectException;
}
